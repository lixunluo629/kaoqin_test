package com.zaxxer.hikari.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.SQLExceptionOverride;
import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.pool.HikariPool;
import com.zaxxer.hikari.util.ClockSource;
import com.zaxxer.hikari.util.DriverDataSource;
import com.zaxxer.hikari.util.PropertyElf;
import com.zaxxer.hikari.util.UtilityElf;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/PoolBase.class */
abstract class PoolBase {
    public final HikariConfig config;
    IMetricsTrackerDelegate metricsTracker;
    protected final String poolName;
    volatile String catalog;
    final AtomicReference<Exception> lastConnectionFailure;
    long connectionTimeout;
    long validationTimeout;
    SQLExceptionOverride exceptionOverride;
    private static final String[] RESET_STATES = {DefaultTransactionDefinition.READ_ONLY_MARKER, "autoCommit", "isolation", "catalog", "netTimeout", "schema"};
    private static final int UNINITIALIZED = -1;
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private int defaultTransactionIsolation;
    private int transactionIsolation;
    private Executor netTimeoutExecutor;
    private DataSource dataSource;
    private final String schema;
    private final boolean isReadOnly;
    private final boolean isAutoCommit;
    private final boolean isUseJdbc4Validation;
    private final boolean isIsolateInternalQueries;
    private volatile boolean isValidChecked;
    private final Logger logger = LoggerFactory.getLogger((Class<?>) PoolBase.class);
    private int networkTimeout = -1;
    private int isQueryTimeoutSupported = -1;
    private int isNetworkTimeoutSupported = -1;

    abstract void recycle(PoolEntry poolEntry);

    PoolBase(HikariConfig config) {
        this.config = config;
        this.catalog = config.getCatalog();
        this.schema = config.getSchema();
        this.isReadOnly = config.isReadOnly();
        this.isAutoCommit = config.isAutoCommit();
        this.exceptionOverride = (SQLExceptionOverride) UtilityElf.createInstance(config.getExceptionOverrideClassName(), SQLExceptionOverride.class, new Object[0]);
        this.transactionIsolation = UtilityElf.getTransactionIsolation(config.getTransactionIsolation());
        this.isUseJdbc4Validation = config.getConnectionTestQuery() == null;
        this.isIsolateInternalQueries = config.isIsolateInternalQueries();
        this.poolName = config.getPoolName();
        this.connectionTimeout = config.getConnectionTimeout();
        this.validationTimeout = config.getValidationTimeout();
        this.lastConnectionFailure = new AtomicReference<>();
        initializeDataSource();
    }

    public String toString() {
        return this.poolName;
    }

    void quietlyCloseConnection(Connection connection, String closureReason) throws SQLException {
        if (connection != null) {
            try {
                this.logger.debug("{} - Closing connection {}: {}", this.poolName, connection, closureReason);
                try {
                    setNetworkTimeout(connection, TimeUnit.SECONDS.toMillis(15L));
                    connection.close();
                } catch (SQLException e) {
                    connection.close();
                } catch (Throwable th) {
                    connection.close();
                    throw th;
                }
            } catch (Exception e2) {
                this.logger.debug("{} - Closing connection {} failed", this.poolName, connection, e2);
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    boolean isConnectionAlive(Connection connection) throws SQLException {
        try {
            try {
                setNetworkTimeout(connection, this.validationTimeout);
                int validationSeconds = ((int) Math.max(1000L, this.validationTimeout)) / 1000;
                if (this.isUseJdbc4Validation) {
                    boolean zIsValid = connection.isValid(validationSeconds);
                    setNetworkTimeout(connection, this.networkTimeout);
                    if (this.isIsolateInternalQueries && !this.isAutoCommit) {
                        connection.rollback();
                    }
                    return zIsValid;
                }
                Statement statement = connection.createStatement();
                try {
                    if (this.isNetworkTimeoutSupported != 1) {
                        setQueryTimeout(statement, validationSeconds);
                    }
                    statement.execute(this.config.getConnectionTestQuery());
                    if (statement != null) {
                        statement.close();
                    }
                    setNetworkTimeout(connection, this.networkTimeout);
                    if (this.isIsolateInternalQueries && !this.isAutoCommit) {
                        connection.rollback();
                        return true;
                    }
                    return true;
                } catch (Throwable th) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                setNetworkTimeout(connection, this.networkTimeout);
                if (this.isIsolateInternalQueries && !this.isAutoCommit) {
                    connection.rollback();
                }
                throw th3;
            }
        } catch (Exception e) {
            this.lastConnectionFailure.set(e);
            this.logger.warn("{} - Failed to validate connection {} ({}). Possibly consider using a shorter maxLifetime value.", this.poolName, connection, e.getMessage());
            return false;
        }
    }

    Exception getLastConnectionFailure() {
        return this.lastConnectionFailure.get();
    }

    public DataSource getUnwrappedDataSource() {
        return this.dataSource;
    }

    PoolEntry newPoolEntry() throws Exception {
        return new PoolEntry(newConnection(), this, this.isReadOnly, this.isAutoCommit);
    }

    void resetConnectionState(Connection connection, ProxyConnection proxyConnection, int dirtyBits) throws SQLException {
        int resetBits = 0;
        if ((dirtyBits & 1) != 0 && proxyConnection.getReadOnlyState() != this.isReadOnly) {
            connection.setReadOnly(this.isReadOnly);
            resetBits = 0 | 1;
        }
        if ((dirtyBits & 2) != 0 && proxyConnection.getAutoCommitState() != this.isAutoCommit) {
            connection.setAutoCommit(this.isAutoCommit);
            resetBits |= 2;
        }
        if ((dirtyBits & 4) != 0 && proxyConnection.getTransactionIsolationState() != this.transactionIsolation) {
            connection.setTransactionIsolation(this.transactionIsolation);
            resetBits |= 4;
        }
        if ((dirtyBits & 8) != 0 && this.catalog != null && !this.catalog.equals(proxyConnection.getCatalogState())) {
            connection.setCatalog(this.catalog);
            resetBits |= 8;
        }
        if ((dirtyBits & 16) != 0 && proxyConnection.getNetworkTimeoutState() != this.networkTimeout) {
            setNetworkTimeout(connection, this.networkTimeout);
            resetBits |= 16;
        }
        if ((dirtyBits & 32) != 0 && this.schema != null && !this.schema.equals(proxyConnection.getSchemaState())) {
            connection.setSchema(this.schema);
            resetBits |= 32;
        }
        if (resetBits != 0 && this.logger.isDebugEnabled()) {
            this.logger.debug("{} - Reset ({}) on connection {}", this.poolName, stringFromResetBits(resetBits), connection);
        }
    }

    void shutdownNetworkTimeoutExecutor() {
        if (this.netTimeoutExecutor instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor) this.netTimeoutExecutor).shutdownNow();
        }
    }

    long getLoginTimeout() {
        try {
            return this.dataSource != null ? this.dataSource.getLoginTimeout() : TimeUnit.SECONDS.toSeconds(5L);
        } catch (SQLException e) {
            return TimeUnit.SECONDS.toSeconds(5L);
        }
    }

    void handleMBeans(HikariPool hikariPool, boolean register) {
        if (!this.config.isRegisterMbeans()) {
            return;
        }
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName beanConfigName = new ObjectName("com.zaxxer.hikari:type=PoolConfig (" + this.poolName + ")");
            ObjectName beanPoolName = new ObjectName("com.zaxxer.hikari:type=Pool (" + this.poolName + ")");
            if (register) {
                if (!mBeanServer.isRegistered(beanConfigName)) {
                    mBeanServer.registerMBean(this.config, beanConfigName);
                    mBeanServer.registerMBean(hikariPool, beanPoolName);
                } else {
                    this.logger.error("{} - JMX name ({}) is already registered.", this.poolName, this.poolName);
                }
            } else if (mBeanServer.isRegistered(beanConfigName)) {
                mBeanServer.unregisterMBean(beanConfigName);
                mBeanServer.unregisterMBean(beanPoolName);
            }
        } catch (Exception e) {
            Logger logger = this.logger;
            Object[] objArr = new Object[3];
            objArr[0] = this.poolName;
            objArr[1] = register ? "register" : "unregister";
            objArr[2] = e;
            logger.warn("{} - Failed to {} management beans.", objArr);
        }
    }

    private void initializeDataSource() {
        String jdbcUrl = this.config.getJdbcUrl();
        String username = this.config.getUsername();
        String password = this.config.getPassword();
        String dsClassName = this.config.getDataSourceClassName();
        String driverClassName = this.config.getDriverClassName();
        String dataSourceJNDI = this.config.getDataSourceJNDI();
        Properties dataSourceProperties = this.config.getDataSourceProperties();
        DataSource ds = this.config.getDataSource();
        if (dsClassName != null && ds == null) {
            ds = (DataSource) UtilityElf.createInstance(dsClassName, DataSource.class, new Object[0]);
            PropertyElf.setTargetFromProperties(ds, dataSourceProperties);
        } else if (jdbcUrl != null && ds == null) {
            ds = new DriverDataSource(jdbcUrl, driverClassName, dataSourceProperties, username, password);
        } else if (dataSourceJNDI != null && ds == null) {
            try {
                InitialContext ic = new InitialContext();
                ds = (DataSource) ic.lookup(dataSourceJNDI);
            } catch (NamingException e) {
                throw new HikariPool.PoolInitializationException(e);
            }
        }
        if (ds != null) {
            setLoginTimeout(ds);
            createNetworkTimeoutExecutor(ds, dsClassName, jdbcUrl);
        }
        this.dataSource = ds;
    }

    private Connection newConnection() throws Exception {
        long start = ClockSource.currentTime();
        try {
            try {
                String username = this.config.getUsername();
                String password = this.config.getPassword();
                Connection connection = username == null ? this.dataSource.getConnection() : this.dataSource.getConnection(username, password);
                if (connection == null) {
                    throw new SQLTransientConnectionException("DataSource returned null unexpectedly");
                }
                setupConnection(connection);
                this.lastConnectionFailure.set(null);
                if (this.metricsTracker != null) {
                    this.metricsTracker.recordConnectionCreated(ClockSource.elapsedMillis(start));
                }
                return connection;
            } catch (Exception e) {
                if (0 != 0) {
                    quietlyCloseConnection(null, "(Failed to create/setup connection)");
                } else if (getLastConnectionFailure() == null) {
                    this.logger.debug("{} - Failed to create/setup connection: {}", this.poolName, e.getMessage());
                }
                this.lastConnectionFailure.set(e);
                throw e;
            }
        } catch (Throwable th) {
            if (this.metricsTracker != null) {
                this.metricsTracker.recordConnectionCreated(ClockSource.elapsedMillis(start));
            }
            throw th;
        }
    }

    private void setupConnection(Connection connection) throws ConnectionSetupException, SQLException {
        try {
            if (this.networkTimeout == -1) {
                this.networkTimeout = getAndSetNetworkTimeout(connection, this.validationTimeout);
            } else {
                setNetworkTimeout(connection, this.validationTimeout);
            }
            if (connection.isReadOnly() != this.isReadOnly) {
                connection.setReadOnly(this.isReadOnly);
            }
            if (connection.getAutoCommit() != this.isAutoCommit) {
                connection.setAutoCommit(this.isAutoCommit);
            }
            checkDriverSupport(connection);
            if (this.transactionIsolation != this.defaultTransactionIsolation) {
                connection.setTransactionIsolation(this.transactionIsolation);
            }
            if (this.catalog != null) {
                connection.setCatalog(this.catalog);
            }
            if (this.schema != null) {
                connection.setSchema(this.schema);
            }
            executeSql(connection, this.config.getConnectionInitSql(), true);
            setNetworkTimeout(connection, this.networkTimeout);
        } catch (SQLException e) {
            throw new ConnectionSetupException(e);
        }
    }

    private void checkDriverSupport(Connection connection) throws SQLException {
        if (!this.isValidChecked) {
            checkValidationSupport(connection);
            checkDefaultIsolation(connection);
            this.isValidChecked = true;
        }
    }

    private void checkValidationSupport(Connection connection) throws SQLException {
        try {
            if (this.isUseJdbc4Validation) {
                connection.isValid(1);
            } else {
                executeSql(connection, this.config.getConnectionTestQuery(), false);
            }
        } catch (AbstractMethodError | Exception e) {
            Logger logger = this.logger;
            Object[] objArr = new Object[3];
            objArr[0] = this.poolName;
            objArr[1] = this.isUseJdbc4Validation ? " isValid() for connection, configure" : "";
            objArr[2] = e.getMessage();
            logger.error("{} - Failed to execute{} connection test query ({}).", objArr);
            throw e;
        }
    }

    private void checkDefaultIsolation(Connection connection) throws SQLException {
        try {
            this.defaultTransactionIsolation = connection.getTransactionIsolation();
            if (this.transactionIsolation == -1) {
                this.transactionIsolation = this.defaultTransactionIsolation;
            }
        } catch (SQLException e) {
            this.logger.warn("{} - Default transaction isolation level detection failed ({}).", this.poolName, e.getMessage());
            if (e.getSQLState() != null && !e.getSQLState().startsWith("08")) {
                throw e;
            }
        }
    }

    private void setQueryTimeout(Statement statement, int timeoutSec) throws SQLException {
        if (this.isQueryTimeoutSupported != 0) {
            try {
                statement.setQueryTimeout(timeoutSec);
                this.isQueryTimeoutSupported = 1;
            } catch (Exception e) {
                if (this.isQueryTimeoutSupported == -1) {
                    this.isQueryTimeoutSupported = 0;
                    this.logger.info("{} - Failed to set query timeout for statement. ({})", this.poolName, e.getMessage());
                }
            }
        }
    }

    private int getAndSetNetworkTimeout(Connection connection, long timeoutMs) {
        if (this.isNetworkTimeoutSupported != 0) {
            try {
                int originalTimeout = connection.getNetworkTimeout();
                connection.setNetworkTimeout(this.netTimeoutExecutor, (int) timeoutMs);
                this.isNetworkTimeoutSupported = 1;
                return originalTimeout;
            } catch (AbstractMethodError | Exception e) {
                if (this.isNetworkTimeoutSupported == -1) {
                    this.isNetworkTimeoutSupported = 0;
                    this.logger.info("{} - Driver does not support get/set network timeout for connections. ({})", this.poolName, e.getMessage());
                    if (this.validationTimeout < TimeUnit.SECONDS.toMillis(1L)) {
                        this.logger.warn("{} - A validationTimeout of less than 1 second cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
                        return 0;
                    }
                    if (this.validationTimeout % TimeUnit.SECONDS.toMillis(1L) != 0) {
                        this.logger.warn("{} - A validationTimeout with fractional second granularity cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
                        return 0;
                    }
                    return 0;
                }
                return 0;
            }
        }
        return 0;
    }

    private void setNetworkTimeout(Connection connection, long timeoutMs) throws SQLException {
        if (this.isNetworkTimeoutSupported == 1) {
            connection.setNetworkTimeout(this.netTimeoutExecutor, (int) timeoutMs);
        }
    }

    private void executeSql(Connection connection, String sql, boolean isCommit) throws SQLException {
        if (sql != null) {
            Statement statement = connection.createStatement();
            try {
                statement.execute(sql);
                if (statement != null) {
                    statement.close();
                }
                if (this.isIsolateInternalQueries && !this.isAutoCommit) {
                    if (isCommit) {
                        connection.commit();
                    } else {
                        connection.rollback();
                    }
                }
            } catch (Throwable th) {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    private void createNetworkTimeoutExecutor(DataSource dataSource, String dsClassName, String jdbcUrl) {
        if ((dsClassName != null && dsClassName.contains("Mysql")) || ((jdbcUrl != null && jdbcUrl.contains("mysql")) || (dataSource != null && dataSource.getClass().getName().contains("Mysql")))) {
            this.netTimeoutExecutor = new SynchronousExecutor();
            return;
        }
        ThreadFactory threadFactory = this.config.getThreadFactory();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool(threadFactory != null ? threadFactory : new UtilityElf.DefaultThreadFactory(this.poolName + " network timeout executor", true));
        executor.setKeepAliveTime(15L, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        this.netTimeoutExecutor = executor;
    }

    private void setLoginTimeout(DataSource dataSource) {
        if (this.connectionTimeout != 2147483647L) {
            try {
                dataSource.setLoginTimeout(Math.max(1, (int) TimeUnit.MILLISECONDS.toSeconds(500 + this.connectionTimeout)));
            } catch (Exception e) {
                this.logger.info("{} - Failed to set login timeout for data source. ({})", this.poolName, e.getMessage());
            }
        }
    }

    private String stringFromResetBits(int bits) {
        StringBuilder sb = new StringBuilder();
        for (int ndx = 0; ndx < RESET_STATES.length; ndx++) {
            if ((bits & (1 << ndx)) != 0) {
                sb.append(RESET_STATES[ndx]).append(", ");
            }
        }
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/PoolBase$ConnectionSetupException.class */
    static class ConnectionSetupException extends Exception {
        private static final long serialVersionUID = 929872118275916521L;

        ConnectionSetupException(Throwable t) {
            super(t);
        }
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/PoolBase$SynchronousExecutor.class */
    private static class SynchronousExecutor implements Executor {
        private SynchronousExecutor() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable command) {
            try {
                command.run();
            } catch (Exception t) {
                LoggerFactory.getLogger((Class<?>) PoolBase.class).debug("Failed to execute: {}", command, t);
            }
        }
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/PoolBase$IMetricsTrackerDelegate.class */
    interface IMetricsTrackerDelegate extends AutoCloseable {
        default void recordConnectionUsage(PoolEntry poolEntry) {
        }

        default void recordConnectionCreated(long connectionCreatedMillis) {
        }

        default void recordBorrowTimeoutStats(long startTime) {
        }

        default void recordBorrowStats(PoolEntry poolEntry, long startTime) {
        }

        default void recordConnectionTimeout() {
        }

        @Override // java.lang.AutoCloseable
        default void close() {
        }
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/PoolBase$MetricsTrackerDelegate.class */
    static class MetricsTrackerDelegate implements IMetricsTrackerDelegate {
        final IMetricsTracker tracker;

        MetricsTrackerDelegate(IMetricsTracker tracker) {
            this.tracker = tracker;
        }

        @Override // com.zaxxer.hikari.pool.PoolBase.IMetricsTrackerDelegate
        public void recordConnectionUsage(PoolEntry poolEntry) {
            this.tracker.recordConnectionUsageMillis(poolEntry.getMillisSinceBorrowed());
        }

        @Override // com.zaxxer.hikari.pool.PoolBase.IMetricsTrackerDelegate
        public void recordConnectionCreated(long connectionCreatedMillis) {
            this.tracker.recordConnectionCreatedMillis(connectionCreatedMillis);
        }

        @Override // com.zaxxer.hikari.pool.PoolBase.IMetricsTrackerDelegate
        public void recordBorrowTimeoutStats(long startTime) {
            this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime));
        }

        @Override // com.zaxxer.hikari.pool.PoolBase.IMetricsTrackerDelegate
        public void recordBorrowStats(PoolEntry poolEntry, long startTime) {
            long now = ClockSource.currentTime();
            poolEntry.lastBorrowed = now;
            this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime, now));
        }

        @Override // com.zaxxer.hikari.pool.PoolBase.IMetricsTrackerDelegate
        public void recordConnectionTimeout() {
            this.tracker.recordConnectionTimeout();
        }

        @Override // com.zaxxer.hikari.pool.PoolBase.IMetricsTrackerDelegate, java.lang.AutoCloseable
        public void close() {
            this.tracker.close();
        }
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/PoolBase$NopMetricsTrackerDelegate.class */
    static final class NopMetricsTrackerDelegate implements IMetricsTrackerDelegate {
        NopMetricsTrackerDelegate() {
        }
    }
}
