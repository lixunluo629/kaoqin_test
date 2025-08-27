package org.ehcache.impl.internal.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.ehcache.core.spi.service.ExecutionService;
import org.ehcache.impl.internal.util.ThreadFactoryUtil;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceProvider;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/OnDemandExecutionService.class */
public class OnDemandExecutionService implements ExecutionService {
    private static final RejectedExecutionHandler WAIT_FOR_SPACE = new RejectedExecutionHandler() { // from class: org.ehcache.impl.internal.executor.OnDemandExecutionService.1
        @Override // java.util.concurrent.RejectedExecutionHandler
        public void rejectedExecution(Runnable r, ThreadPoolExecutor tpe) {
            boolean interrupted = false;
            while (true) {
                try {
                    tpe.getQueue().put(r);
                    break;
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
    };

    @Override // org.ehcache.core.spi.service.ExecutionService
    public ScheduledExecutorService getScheduledExecutor(String poolAlias) {
        return Executors.unconfigurableScheduledExecutorService(Executors.newSingleThreadScheduledExecutor(ThreadFactoryUtil.threadFactory(poolAlias)));
    }

    @Override // org.ehcache.core.spi.service.ExecutionService
    public ExecutorService getOrderedExecutor(String poolAlias, BlockingQueue<Runnable> queue) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 30L, TimeUnit.SECONDS, queue, ThreadFactoryUtil.threadFactory(poolAlias), WAIT_FOR_SPACE);
        executor.allowCoreThreadTimeOut(true);
        return Executors.unconfigurableExecutorService(executor);
    }

    @Override // org.ehcache.core.spi.service.ExecutionService
    public ExecutorService getUnorderedExecutor(String poolAlias, BlockingQueue<Runnable> queue) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors(), 30L, TimeUnit.SECONDS, queue, ThreadFactoryUtil.threadFactory(poolAlias), WAIT_FOR_SPACE);
        executor.allowCoreThreadTimeOut(true);
        return Executors.unconfigurableExecutorService(executor);
    }

    @Override // org.ehcache.spi.service.Service
    public void start(ServiceProvider<Service> serviceProvider) {
    }

    @Override // org.ehcache.spi.service.Service
    public void stop() {
    }
}
