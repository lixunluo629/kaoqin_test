package com.zaxxer.hikari.pool;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariPoolMXBean;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;
import com.zaxxer.hikari.metrics.dropwizard.CodahaleHealthChecker;
import com.zaxxer.hikari.metrics.dropwizard.CodahaleMetricsTrackerFactory;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
import com.zaxxer.hikari.pool.PoolBase;
import com.zaxxer.hikari.util.ClockSource;
import com.zaxxer.hikari.util.ConcurrentBag;
import com.zaxxer.hikari.util.SuspendResumeLock;
import com.zaxxer.hikari.util.UtilityElf;
import io.micrometer.core.instrument.MeterRegistry;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/HikariPool.class */
public final class HikariPool extends PoolBase implements HikariPoolMXBean, ConcurrentBag.IBagStateListener {
    private final Logger logger;
    public static final int POOL_NORMAL = 0;
    public static final int POOL_SUSPENDED = 1;
    public static final int POOL_SHUTDOWN = 2;
    public volatile int poolState;
    private final long aliveBypassWindowMs;
    private final long housekeepingPeriodMs;
    private static final String EVICTED_CONNECTION_MESSAGE = "(connection was evicted)";
    private static final String DEAD_CONNECTION_MESSAGE = "(connection is dead)";
    private final PoolEntryCreator poolEntryCreator;
    private final PoolEntryCreator postFillPoolEntryCreator;
    private final Collection<Runnable> addConnectionQueueReadOnlyView;
    private final ThreadPoolExecutor addConnectionExecutor;
    private final ThreadPoolExecutor closeConnectionExecutor;
    private final ConcurrentBag<PoolEntry> connectionBag;
    private final ProxyLeakTaskFactory leakTaskFactory;
    private final SuspendResumeLock suspendResumeLock;
    private final ScheduledExecutorService houseKeepingExecutorService;
    private ScheduledFuture<?> houseKeeperTask;

    @Override // com.zaxxer.hikari.pool.PoolBase
    public /* bridge */ /* synthetic */ DataSource getUnwrappedDataSource() {
        return super.getUnwrappedDataSource();
    }

    @Override // com.zaxxer.hikari.pool.PoolBase
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public HikariPool(HikariConfig config) throws InterruptedException {
        super(config);
        this.logger = LoggerFactory.getLogger((Class<?>) HikariPool.class);
        this.aliveBypassWindowMs = Long.getLong("com.zaxxer.hikari.aliveBypassWindowMs", TimeUnit.MILLISECONDS.toMillis(500L)).longValue();
        this.housekeepingPeriodMs = Long.getLong("com.zaxxer.hikari.housekeeping.periodMs", TimeUnit.SECONDS.toMillis(30L)).longValue();
        this.poolEntryCreator = new PoolEntryCreator(null);
        this.postFillPoolEntryCreator = new PoolEntryCreator("After adding ");
        this.connectionBag = new ConcurrentBag<>(this);
        this.suspendResumeLock = config.isAllowPoolSuspension() ? new SuspendResumeLock() : SuspendResumeLock.FAUX_LOCK;
        this.houseKeepingExecutorService = initializeHouseKeepingExecutorService();
        checkFailFast();
        if (config.getMetricsTrackerFactory() != null) {
            setMetricsTrackerFactory(config.getMetricsTrackerFactory());
        } else {
            setMetricRegistry(config.getMetricRegistry());
        }
        setHealthCheckRegistry(config.getHealthCheckRegistry());
        handleMBeans(this, true);
        ThreadFactory threadFactory = config.getThreadFactory();
        int maxPoolSize = config.getMaximumPoolSize();
        LinkedBlockingQueue<Runnable> addConnectionQueue = new LinkedBlockingQueue<>(maxPoolSize);
        this.addConnectionQueueReadOnlyView = Collections.unmodifiableCollection(addConnectionQueue);
        this.addConnectionExecutor = UtilityElf.createThreadPoolExecutor(addConnectionQueue, this.poolName + " connection adder", threadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
        this.closeConnectionExecutor = UtilityElf.createThreadPoolExecutor(maxPoolSize, this.poolName + " connection closer", threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        this.leakTaskFactory = new ProxyLeakTaskFactory(config.getLeakDetectionThreshold(), this.houseKeepingExecutorService);
        this.houseKeeperTask = this.houseKeepingExecutorService.scheduleWithFixedDelay(new HouseKeeper(), 100L, this.housekeepingPeriodMs, TimeUnit.MILLISECONDS);
        if (Boolean.getBoolean("com.zaxxer.hikari.blockUntilFilled") && config.getInitializationFailTimeout() > 1) {
            this.addConnectionExecutor.setCorePoolSize(Math.min(16, Runtime.getRuntime().availableProcessors()));
            this.addConnectionExecutor.setMaximumPoolSize(Math.min(16, Runtime.getRuntime().availableProcessors()));
            long startTime = ClockSource.currentTime();
            while (ClockSource.elapsedMillis(startTime) < config.getInitializationFailTimeout() && getTotalConnections() < config.getMinimumIdle()) {
                UtilityElf.quietlySleep(TimeUnit.MILLISECONDS.toMillis(100L));
            }
            this.addConnectionExecutor.setCorePoolSize(1);
            this.addConnectionExecutor.setMaximumPoolSize(1);
        }
    }

    public Connection getConnection() throws SQLException {
        return getConnection(this.connectionTimeout);
    }

    public Connection getConnection(long hardTimeout) throws SQLException {
        this.suspendResumeLock.acquire();
        long startTime = ClockSource.currentTime();
        long timeout = hardTimeout;
        do {
            try {
                try {
                    PoolEntry poolEntry = (PoolEntry) this.connectionBag.borrow(timeout, TimeUnit.MILLISECONDS);
                    if (poolEntry == null) {
                        break;
                    }
                    long now = ClockSource.currentTime();
                    if (poolEntry.isMarkedEvicted() || (ClockSource.elapsedMillis(poolEntry.lastAccessed, now) > this.aliveBypassWindowMs && !isConnectionAlive(poolEntry.connection))) {
                        closeConnection(poolEntry, poolEntry.isMarkedEvicted() ? EVICTED_CONNECTION_MESSAGE : DEAD_CONNECTION_MESSAGE);
                        timeout = hardTimeout - ClockSource.elapsedMillis(startTime);
                    } else {
                        this.metricsTracker.recordBorrowStats(poolEntry, startTime);
                        Connection connectionCreateProxyConnection = poolEntry.createProxyConnection(this.leakTaskFactory.schedule(poolEntry), now);
                        this.suspendResumeLock.release();
                        return connectionCreateProxyConnection;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new SQLException(this.poolName + " - Interrupted during connection acquisition", e);
                }
            } catch (Throwable th) {
                this.suspendResumeLock.release();
                throw th;
            }
        } while (timeout > 0);
        this.metricsTracker.recordBorrowTimeoutStats(startTime);
        throw createTimeoutException(startTime);
    }

    /* JADX WARN: Finally extract failed */
    public synchronized void shutdown() throws InterruptedException {
        try {
            this.poolState = 2;
            if (this.addConnectionExecutor != null) {
                logPoolState("Before shutdown ");
                if (this.houseKeeperTask != null) {
                    this.houseKeeperTask.cancel(false);
                    this.houseKeeperTask = null;
                }
                softEvictConnections();
                this.addConnectionExecutor.shutdown();
                this.addConnectionExecutor.awaitTermination(getLoginTimeout(), TimeUnit.SECONDS);
                destroyHouseKeepingExecutorService();
                this.connectionBag.close();
                ExecutorService assassinExecutor = UtilityElf.createThreadPoolExecutor(this.config.getMaximumPoolSize(), this.poolName + " connection assassinator", this.config.getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
                try {
                    long start = ClockSource.currentTime();
                    do {
                        abortActiveConnections(assassinExecutor);
                        softEvictConnections();
                        if (getTotalConnections() <= 0) {
                            break;
                        }
                    } while (ClockSource.elapsedMillis(start) < TimeUnit.SECONDS.toMillis(10L));
                    assassinExecutor.shutdown();
                    assassinExecutor.awaitTermination(10L, TimeUnit.SECONDS);
                    shutdownNetworkTimeoutExecutor();
                    this.closeConnectionExecutor.shutdown();
                    this.closeConnectionExecutor.awaitTermination(10L, TimeUnit.SECONDS);
                    logPoolState("After shutdown ");
                    handleMBeans(this, false);
                    this.metricsTracker.close();
                    return;
                } catch (Throwable th) {
                    assassinExecutor.shutdown();
                    assassinExecutor.awaitTermination(10L, TimeUnit.SECONDS);
                    throw th;
                }
            }
            logPoolState("After shutdown ");
            handleMBeans(this, false);
            this.metricsTracker.close();
        } catch (Throwable th2) {
            logPoolState("After shutdown ");
            handleMBeans(this, false);
            this.metricsTracker.close();
            throw th2;
        }
    }

    public void evictConnection(Connection connection) {
        ProxyConnection proxyConnection = (ProxyConnection) connection;
        proxyConnection.cancelLeakTask();
        try {
            softEvictConnection(proxyConnection.getPoolEntry(), "(connection evicted by user)", !connection.isClosed());
        } catch (SQLException e) {
        }
    }

    public void setMetricRegistry(Object metricRegistry) {
        if (metricRegistry != null && UtilityElf.safeIsAssignableFrom(metricRegistry, "com.codahale.metrics.MetricRegistry")) {
            setMetricsTrackerFactory(new CodahaleMetricsTrackerFactory((MetricRegistry) metricRegistry));
        } else if (metricRegistry != null && UtilityElf.safeIsAssignableFrom(metricRegistry, "io.micrometer.core.instrument.MeterRegistry")) {
            setMetricsTrackerFactory(new MicrometerMetricsTrackerFactory((MeterRegistry) metricRegistry));
        } else {
            setMetricsTrackerFactory(null);
        }
    }

    public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
        if (metricsTrackerFactory != null) {
            this.metricsTracker = new PoolBase.MetricsTrackerDelegate(metricsTrackerFactory.create(this.config.getPoolName(), getPoolStats()));
        } else {
            this.metricsTracker = new PoolBase.NopMetricsTrackerDelegate();
        }
    }

    public void setHealthCheckRegistry(Object healthCheckRegistry) {
        if (healthCheckRegistry != null) {
            CodahaleHealthChecker.registerHealthChecks(this, this.config, (HealthCheckRegistry) healthCheckRegistry);
        }
    }

    @Override // com.zaxxer.hikari.util.ConcurrentBag.IBagStateListener
    public void addBagItem(int waiting) {
        boolean shouldAdd = waiting - this.addConnectionQueueReadOnlyView.size() >= 0;
        if (shouldAdd) {
            this.addConnectionExecutor.submit(this.poolEntryCreator);
        } else {
            this.logger.debug("{} - Add connection elided, waiting {}, queue {}", this.poolName, Integer.valueOf(waiting), Integer.valueOf(this.addConnectionQueueReadOnlyView.size()));
        }
    }

    @Override // com.zaxxer.hikari.HikariPoolMXBean
    public int getActiveConnections() {
        return this.connectionBag.getCount(1);
    }

    @Override // com.zaxxer.hikari.HikariPoolMXBean
    public int getIdleConnections() {
        return this.connectionBag.getCount(0);
    }

    @Override // com.zaxxer.hikari.HikariPoolMXBean
    public int getTotalConnections() {
        return this.connectionBag.size();
    }

    @Override // com.zaxxer.hikari.HikariPoolMXBean
    public int getThreadsAwaitingConnection() {
        return this.connectionBag.getWaitingThreadCount();
    }

    @Override // com.zaxxer.hikari.HikariPoolMXBean
    public void softEvictConnections() {
        this.connectionBag.values().forEach(poolEntry -> {
            softEvictConnection(poolEntry, "(connection evicted)", false);
        });
    }

    @Override // com.zaxxer.hikari.HikariPoolMXBean
    public synchronized void suspendPool() {
        if (this.suspendResumeLock == SuspendResumeLock.FAUX_LOCK) {
            throw new IllegalStateException(this.poolName + " - is not suspendable");
        }
        if (this.poolState != 1) {
            this.suspendResumeLock.suspend();
            this.poolState = 1;
        }
    }

    @Override // com.zaxxer.hikari.HikariPoolMXBean
    public synchronized void resumePool() {
        if (this.poolState == 1) {
            this.poolState = 0;
            fillPool();
            this.suspendResumeLock.resume();
        }
    }

    void logPoolState(String... prefix) {
        if (this.logger.isDebugEnabled()) {
            Logger logger = this.logger;
            Object[] objArr = new Object[6];
            objArr[0] = this.poolName;
            objArr[1] = prefix.length > 0 ? prefix[0] : "";
            objArr[2] = Integer.valueOf(getTotalConnections());
            objArr[3] = Integer.valueOf(getActiveConnections());
            objArr[4] = Integer.valueOf(getIdleConnections());
            objArr[5] = Integer.valueOf(getThreadsAwaitingConnection());
            logger.debug("{} - {}stats (total={}, active={}, idle={}, waiting={})", objArr);
        }
    }

    @Override // com.zaxxer.hikari.pool.PoolBase
    void recycle(PoolEntry poolEntry) {
        this.metricsTracker.recordConnectionUsage(poolEntry);
        this.connectionBag.requite(poolEntry);
    }

    void closeConnection(PoolEntry poolEntry, String closureReason) {
        if (this.connectionBag.remove(poolEntry)) {
            Connection connection = poolEntry.close();
            this.closeConnectionExecutor.execute(() -> {
                quietlyCloseConnection(connection, closureReason);
                if (this.poolState == 0) {
                    fillPool();
                }
            });
        }
    }

    int[] getPoolStateCounts() {
        return this.connectionBag.getStateCounts();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PoolEntry createPoolEntry() {
        try {
            PoolEntry poolEntry = newPoolEntry();
            long maxLifetime = this.config.getMaxLifetime();
            if (maxLifetime > 0) {
                long variance = maxLifetime > 10000 ? ThreadLocalRandom.current().nextLong(maxLifetime / 40) : 0L;
                long lifetime = maxLifetime - variance;
                poolEntry.setFutureEol(this.houseKeepingExecutorService.schedule(() -> {
                    if (softEvictConnection(poolEntry, "(connection has passed maxLifetime)", false)) {
                        addBagItem(this.connectionBag.getWaitingThreadCount());
                    }
                }, lifetime, TimeUnit.MILLISECONDS));
            }
            return poolEntry;
        } catch (PoolBase.ConnectionSetupException e) {
            if (this.poolState == 0) {
                this.logger.error("{} - Error thrown while acquiring connection from data source", this.poolName, e.getCause());
                this.lastConnectionFailure.set(e);
                return null;
            }
            return null;
        } catch (Exception e2) {
            if (this.poolState == 0) {
                this.logger.debug("{} - Cannot acquire connection from data source", this.poolName, e2);
                return null;
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void fillPool() {
        int connectionsToAdd = Math.min(this.config.getMaximumPoolSize() - getTotalConnections(), this.config.getMinimumIdle() - getIdleConnections()) - this.addConnectionQueueReadOnlyView.size();
        if (connectionsToAdd <= 0) {
            this.logger.debug("{} - Fill pool skipped, pool is at sufficient level.", this.poolName);
        }
        int i = 0;
        while (i < connectionsToAdd) {
            this.addConnectionExecutor.submit(i < connectionsToAdd - 1 ? this.poolEntryCreator : this.postFillPoolEntryCreator);
            i++;
        }
    }

    private void abortActiveConnections(ExecutorService assassinExecutor) {
        for (T poolEntry : this.connectionBag.values(1)) {
            Connection connection = poolEntry.close();
            try {
                try {
                    connection.abort(assassinExecutor);
                    this.connectionBag.remove(poolEntry);
                } catch (Throwable th) {
                    quietlyCloseConnection(connection, "(connection aborted during shutdown)");
                    this.connectionBag.remove(poolEntry);
                }
            } catch (Throwable th2) {
                this.connectionBag.remove(poolEntry);
                throw th2;
            }
        }
    }

    private void checkFailFast() throws InterruptedException {
        long initializationTimeout = this.config.getInitializationFailTimeout();
        if (initializationTimeout < 0) {
            return;
        }
        long startTime = ClockSource.currentTime();
        do {
            PoolEntry poolEntry = createPoolEntry();
            if (poolEntry != null) {
                if (this.config.getMinimumIdle() > 0) {
                    this.connectionBag.add(poolEntry);
                    this.logger.debug("{} - Added connection {}", this.poolName, poolEntry.connection);
                    return;
                } else {
                    quietlyCloseConnection(poolEntry.close(), "(initialization check complete and minimumIdle is zero)");
                    return;
                }
            }
            if (getLastConnectionFailure() instanceof PoolBase.ConnectionSetupException) {
                throwPoolInitializationException(getLastConnectionFailure().getCause());
            }
            UtilityElf.quietlySleep(TimeUnit.SECONDS.toMillis(1L));
        } while (ClockSource.elapsedMillis(startTime) < initializationTimeout);
        if (initializationTimeout > 0) {
            throwPoolInitializationException(getLastConnectionFailure());
        }
    }

    private void throwPoolInitializationException(Throwable t) {
        this.logger.error("{} - Exception during pool initialization.", this.poolName, t);
        destroyHouseKeepingExecutorService();
        throw new PoolInitializationException(t);
    }

    private boolean softEvictConnection(PoolEntry poolEntry, String reason, boolean owner) {
        poolEntry.markEvicted();
        if (owner || this.connectionBag.reserve(poolEntry)) {
            closeConnection(poolEntry, reason);
            return true;
        }
        return false;
    }

    private ScheduledExecutorService initializeHouseKeepingExecutorService() {
        if (this.config.getScheduledExecutor() == null) {
            ThreadFactory threadFactory = (ThreadFactory) Optional.ofNullable(this.config.getThreadFactory()).orElseGet(() -> {
                return new UtilityElf.DefaultThreadFactory(this.poolName + " housekeeper", true);
            });
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory, new ThreadPoolExecutor.DiscardPolicy());
            executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            executor.setRemoveOnCancelPolicy(true);
            return executor;
        }
        return this.config.getScheduledExecutor();
    }

    private void destroyHouseKeepingExecutorService() {
        if (this.config.getScheduledExecutor() == null) {
            this.houseKeepingExecutorService.shutdownNow();
        }
    }

    private PoolStats getPoolStats() {
        return new PoolStats(TimeUnit.SECONDS.toMillis(1L)) { // from class: com.zaxxer.hikari.pool.HikariPool.1
            @Override // com.zaxxer.hikari.metrics.PoolStats
            protected void update() {
                this.pendingThreads = HikariPool.this.getThreadsAwaitingConnection();
                this.idleConnections = HikariPool.this.getIdleConnections();
                this.totalConnections = HikariPool.this.getTotalConnections();
                this.activeConnections = HikariPool.this.getActiveConnections();
                this.maxConnections = HikariPool.this.config.getMaximumPoolSize();
                this.minConnections = HikariPool.this.config.getMinimumIdle();
            }
        };
    }

    private SQLException createTimeoutException(long startTime) {
        logPoolState("Timeout failure ");
        this.metricsTracker.recordConnectionTimeout();
        String sqlState = null;
        Throwable originalException = getLastConnectionFailure();
        if (originalException instanceof SQLException) {
            sqlState = ((SQLException) originalException).getSQLState();
        }
        SQLException connectionException = new SQLTransientConnectionException(this.poolName + " - Connection is not available, request timed out after " + ClockSource.elapsedMillis(startTime) + "ms.", sqlState, originalException);
        if (originalException instanceof SQLException) {
            connectionException.setNextException((SQLException) originalException);
        }
        return connectionException;
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/HikariPool$PoolEntryCreator.class */
    private final class PoolEntryCreator implements Callable<Boolean> {
        private final String loggingPrefix;

        PoolEntryCreator(String loggingPrefix) {
            this.loggingPrefix = loggingPrefix;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x00aa, code lost:
        
            return java.lang.Boolean.FALSE;
         */
        @Override // java.util.concurrent.Callable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.Boolean call() throws java.lang.InterruptedException {
            /*
                r9 = this;
                r0 = 250(0xfa, double:1.235E-321)
                r10 = r0
            L4:
                r0 = r9
                com.zaxxer.hikari.pool.HikariPool r0 = com.zaxxer.hikari.pool.HikariPool.this
                int r0 = r0.poolState
                if (r0 != 0) goto La7
                r0 = r9
                boolean r0 = r0.shouldCreateAnotherConnection()
                if (r0 == 0) goto La7
                r0 = r9
                com.zaxxer.hikari.pool.HikariPool r0 = com.zaxxer.hikari.pool.HikariPool.this
                com.zaxxer.hikari.pool.PoolEntry r0 = com.zaxxer.hikari.pool.HikariPool.access$100(r0)
                r12 = r0
                r0 = r12
                if (r0 == 0) goto L62
                r0 = r9
                com.zaxxer.hikari.pool.HikariPool r0 = com.zaxxer.hikari.pool.HikariPool.this
                com.zaxxer.hikari.util.ConcurrentBag r0 = com.zaxxer.hikari.pool.HikariPool.access$200(r0)
                r1 = r12
                r0.add(r1)
                r0 = r9
                com.zaxxer.hikari.pool.HikariPool r0 = com.zaxxer.hikari.pool.HikariPool.this
                org.slf4j.Logger r0 = com.zaxxer.hikari.pool.HikariPool.access$300(r0)
                java.lang.String r1 = "{} - Added connection {}"
                r2 = r9
                com.zaxxer.hikari.pool.HikariPool r2 = com.zaxxer.hikari.pool.HikariPool.this
                java.lang.String r2 = r2.poolName
                r3 = r12
                java.sql.Connection r3 = r3.connection
                r0.debug(r1, r2, r3)
                r0 = r9
                java.lang.String r0 = r0.loggingPrefix
                if (r0 == 0) goto L5e
                r0 = r9
                com.zaxxer.hikari.pool.HikariPool r0 = com.zaxxer.hikari.pool.HikariPool.this
                r1 = 1
                java.lang.String[] r1 = new java.lang.String[r1]
                r2 = r1
                r3 = 0
                r4 = r9
                java.lang.String r4 = r4.loggingPrefix
                r2[r3] = r4
                r0.logPoolState(r1)
            L5e:
                java.lang.Boolean r0 = java.lang.Boolean.TRUE
                return r0
            L62:
                r0 = r9
                java.lang.String r0 = r0.loggingPrefix
                if (r0 == 0) goto L82
                r0 = r9
                com.zaxxer.hikari.pool.HikariPool r0 = com.zaxxer.hikari.pool.HikariPool.this
                org.slf4j.Logger r0 = com.zaxxer.hikari.pool.HikariPool.access$300(r0)
                java.lang.String r1 = "{} - Connection add failed, sleeping with backoff: {}ms"
                r2 = r9
                com.zaxxer.hikari.pool.HikariPool r2 = com.zaxxer.hikari.pool.HikariPool.this
                java.lang.String r2 = r2.poolName
                r3 = r10
                java.lang.Long r3 = java.lang.Long.valueOf(r3)
                r0.debug(r1, r2, r3)
            L82:
                r0 = r10
                com.zaxxer.hikari.util.UtilityElf.quietlySleep(r0)
                java.util.concurrent.TimeUnit r0 = java.util.concurrent.TimeUnit.SECONDS
                r1 = 10
                long r0 = r0.toMillis(r1)
                r1 = r9
                com.zaxxer.hikari.pool.HikariPool r1 = com.zaxxer.hikari.pool.HikariPool.this
                long r1 = r1.connectionTimeout
                r2 = r10
                double r2 = (double) r2
                r3 = 4609434218613702656(0x3ff8000000000000, double:1.5)
                double r2 = r2 * r3
                long r2 = (long) r2
                long r1 = java.lang.Math.min(r1, r2)
                long r0 = java.lang.Math.min(r0, r1)
                r10 = r0
                goto L4
            La7:
                java.lang.Boolean r0 = java.lang.Boolean.FALSE
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zaxxer.hikari.pool.HikariPool.PoolEntryCreator.call():java.lang.Boolean");
        }

        private synchronized boolean shouldCreateAnotherConnection() {
            return HikariPool.this.getTotalConnections() < HikariPool.this.config.getMaximumPoolSize() && (HikariPool.this.connectionBag.getWaitingThreadCount() > 0 || HikariPool.this.getIdleConnections() < HikariPool.this.config.getMinimumIdle());
        }
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/HikariPool$HouseKeeper.class */
    private final class HouseKeeper implements Runnable {
        private volatile long previous;

        private HouseKeeper() {
            this.previous = ClockSource.plusMillis(ClockSource.currentTime(), -HikariPool.this.housekeepingPeriodMs);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                HikariPool.this.connectionTimeout = HikariPool.this.config.getConnectionTimeout();
                HikariPool.this.validationTimeout = HikariPool.this.config.getValidationTimeout();
                HikariPool.this.leakTaskFactory.updateLeakDetectionThreshold(HikariPool.this.config.getLeakDetectionThreshold());
                HikariPool.this.catalog = (HikariPool.this.config.getCatalog() == null || HikariPool.this.config.getCatalog().equals(HikariPool.this.catalog)) ? HikariPool.this.catalog : HikariPool.this.config.getCatalog();
                long idleTimeout = HikariPool.this.config.getIdleTimeout();
                long now = ClockSource.currentTime();
                if (ClockSource.plusMillis(now, 128L) < ClockSource.plusMillis(this.previous, HikariPool.this.housekeepingPeriodMs)) {
                    HikariPool.this.logger.warn("{} - Retrograde clock change detected (housekeeper delta={}), soft-evicting connections from pool.", HikariPool.this.poolName, ClockSource.elapsedDisplayString(this.previous, now));
                    this.previous = now;
                    HikariPool.this.softEvictConnections();
                    return;
                }
                if (now > ClockSource.plusMillis(this.previous, (3 * HikariPool.this.housekeepingPeriodMs) / 2)) {
                    HikariPool.this.logger.warn("{} - Thread starvation or clock leap detected (housekeeper delta={}).", HikariPool.this.poolName, ClockSource.elapsedDisplayString(this.previous, now));
                }
                this.previous = now;
                String afterPrefix = "Pool ";
                if (idleTimeout > 0 && HikariPool.this.config.getMinimumIdle() < HikariPool.this.config.getMaximumPoolSize()) {
                    HikariPool.this.logPoolState("Before cleanup ");
                    afterPrefix = "After cleanup  ";
                    List<PoolEntry> notInUse = HikariPool.this.connectionBag.values(0);
                    int toRemove = notInUse.size() - HikariPool.this.config.getMinimumIdle();
                    for (PoolEntry entry : notInUse) {
                        if (toRemove > 0 && ClockSource.elapsedMillis(entry.lastAccessed, now) > idleTimeout && HikariPool.this.connectionBag.reserve(entry)) {
                            HikariPool.this.closeConnection(entry, "(connection has passed idleTimeout)");
                            toRemove--;
                        }
                    }
                }
                HikariPool.this.logPoolState(afterPrefix);
                HikariPool.this.fillPool();
            } catch (Exception e) {
                HikariPool.this.logger.error("Unexpected exception in housekeeping task", (Throwable) e);
            }
        }
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/HikariPool$PoolInitializationException.class */
    public static class PoolInitializationException extends RuntimeException {
        private static final long serialVersionUID = 929872118275916520L;

        public PoolInitializationException(Throwable t) {
            super("Failed to initialize pool: " + t.getMessage(), t);
        }
    }
}
