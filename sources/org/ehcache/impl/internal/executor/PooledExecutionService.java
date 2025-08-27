package org.ehcache.impl.internal.executor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.ehcache.core.spi.service.ExecutionService;
import org.ehcache.impl.config.executor.PooledExecutionServiceConfiguration;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.ehcache.impl.internal.util.ThreadFactoryUtil;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/PooledExecutionService.class */
public class PooledExecutionService implements ExecutionService {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) PooledExecutionService.class);
    private final String defaultPoolAlias;
    private final Map<String, PooledExecutionServiceConfiguration.PoolConfiguration> poolConfigurations;
    private final Map<String, ThreadPoolExecutor> pools = new ConcurrentHashMap(8, 0.75f, 1);
    private volatile boolean running = false;
    private volatile OutOfBandScheduledExecutor scheduledExecutor;

    PooledExecutionService(PooledExecutionServiceConfiguration configuration) {
        this.defaultPoolAlias = configuration.getDefaultPoolAlias();
        this.poolConfigurations = configuration.getPoolConfigurations();
    }

    @Override // org.ehcache.core.spi.service.ExecutionService
    public ScheduledExecutorService getScheduledExecutor(String poolAlias) {
        return new PartitionedScheduledExecutor(this.scheduledExecutor, getUnorderedExecutor(poolAlias, new LinkedBlockingQueue()));
    }

    @Override // org.ehcache.core.spi.service.ExecutionService
    public ExecutorService getOrderedExecutor(String poolAlias, BlockingQueue<Runnable> queue) {
        ThreadPoolExecutor executor = getThreadPoolExecutor(poolAlias);
        return new PartitionedOrderedExecutor(queue, executor);
    }

    @Override // org.ehcache.core.spi.service.ExecutionService
    public ExecutorService getUnorderedExecutor(String poolAlias, BlockingQueue<Runnable> queue) {
        ThreadPoolExecutor executor = getThreadPoolExecutor(poolAlias);
        return new PartitionedUnorderedExecutor(queue, executor, executor.getMaximumPoolSize());
    }

    private ThreadPoolExecutor getThreadPoolExecutor(String poolAlias) {
        if (!this.running) {
            throw new IllegalStateException("Service cannot be used, it isn't running");
        }
        String poolAlias2 = poolAlias == null ? this.defaultPoolAlias : poolAlias;
        if (poolAlias2 == null) {
            throw new IllegalArgumentException("Null pool alias provided and no default pool configured");
        }
        ThreadPoolExecutor executor = this.pools.get(poolAlias2);
        if (executor == null) {
            throw new IllegalArgumentException("Pool '" + poolAlias2 + "' is not in the set of available pools " + this.pools.keySet());
        }
        return executor;
    }

    @Override // org.ehcache.spi.service.Service
    public void start(ServiceProvider<Service> serviceProvider) {
        if (this.poolConfigurations.isEmpty()) {
            throw new IllegalStateException("Pool configuration is empty");
        }
        for (Map.Entry<String, PooledExecutionServiceConfiguration.PoolConfiguration> e : this.poolConfigurations.entrySet()) {
            this.pools.put(e.getKey(), createPool(e.getKey(), e.getValue()));
        }
        if (this.defaultPoolAlias != null) {
            ThreadPoolExecutor defaultPool = this.pools.get(this.defaultPoolAlias);
            if (defaultPool == null) {
                throw new IllegalStateException("Pool for default pool alias is null");
            }
        } else {
            LOGGER.warn("No default pool configured, services requiring thread pools must be configured explicitly using named thread pools");
        }
        this.scheduledExecutor = new OutOfBandScheduledExecutor();
        this.running = true;
    }

    @Override // org.ehcache.spi.service.Service
    public void stop() {
        LOGGER.debug("Shutting down PooledExecutionService");
        this.running = false;
        this.scheduledExecutor.shutdownNow();
        Iterator<Map.Entry<String, ThreadPoolExecutor>> it = this.pools.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ThreadPoolExecutor> e = it.next();
            try {
                destroyPool(e.getKey(), e.getValue());
                it.remove();
            } catch (Throwable th) {
                it.remove();
                throw th;
            }
        }
        try {
            if (!this.scheduledExecutor.awaitTermination(30L, TimeUnit.SECONDS)) {
                LOGGER.warn("Timeout while waiting on scheduler to finish");
            }
        } catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
        }
    }

    private static ThreadPoolExecutor createPool(String alias, PooledExecutionServiceConfiguration.PoolConfiguration config) {
        return new ThreadPoolExecutor(config.minSize(), config.maxSize(), 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(), ThreadFactoryUtil.threadFactory(alias));
    }

    private static void destroyPool(String alias, ThreadPoolExecutor executor) {
        List<Runnable> tasks = executor.shutdownNow();
        if (!tasks.isEmpty()) {
            LOGGER.warn("Tasks remaining in pool '{}' at shutdown: {}", alias, tasks);
        }
        boolean interrupted = false;
        while (!executor.awaitTermination(30L, TimeUnit.SECONDS)) {
            try {
                LOGGER.warn("Still waiting for termination of pool '{}'", alias);
            } catch (InterruptedException e) {
                interrupted = true;
            } catch (Throwable th) {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }
}
