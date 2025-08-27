package org.ehcache.impl.internal.executor;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/PartitionedOrderedExecutor.class */
class PartitionedOrderedExecutor extends AbstractExecutorService {
    private final PartitionedUnorderedExecutor delegate;

    PartitionedOrderedExecutor(BlockingQueue<Runnable> queue, ExecutorService executor) {
        this.delegate = new PartitionedUnorderedExecutor(queue, executor, 1);
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        this.delegate.shutdown();
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        return this.delegate.shutdownNow();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return this.delegate.isShutdown();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return this.delegate.isTerminated();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long time, TimeUnit unit) throws InterruptedException {
        return this.delegate.awaitTermination(time, unit);
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable r) {
        this.delegate.execute(r);
    }
}
