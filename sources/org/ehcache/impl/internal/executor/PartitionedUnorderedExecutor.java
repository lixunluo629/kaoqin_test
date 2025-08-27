package org.ehcache.impl.internal.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/PartitionedUnorderedExecutor.class */
class PartitionedUnorderedExecutor extends AbstractExecutorService {
    private final BlockingQueue<Runnable> queue;
    private final ExecutorService executor;
    private final Semaphore runnerPermit;
    private final int maxWorkers;
    private volatile boolean shutdown;
    private final CountDownLatch termination = new CountDownLatch(1);
    private final Set<Thread> liveThreads = new CopyOnWriteArraySet();

    PartitionedUnorderedExecutor(BlockingQueue<Runnable> queue, ExecutorService executor, int maxWorkers) {
        this.queue = queue;
        this.executor = executor;
        this.maxWorkers = maxWorkers;
        this.runnerPermit = new Semaphore(maxWorkers);
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        this.shutdown = true;
        if (isTerminated()) {
            this.termination.countDown();
        }
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        this.shutdown = true;
        if (isTerminated()) {
            this.termination.countDown();
            return Collections.emptyList();
        }
        List<Runnable> failed = new ArrayList<>(this.queue.size());
        this.queue.drainTo(failed);
        for (Thread t : this.liveThreads) {
            t.interrupt();
        }
        return failed;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return this.shutdown;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return isShutdown() && this.queue.isEmpty() && this.runnerPermit.availablePermits() == this.maxWorkers;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long time, TimeUnit unit) throws InterruptedException {
        if (isTerminated()) {
            return true;
        }
        return this.termination.await(time, unit);
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable r) {
        if (this.shutdown) {
            throw new RejectedExecutionException("Executor is shutting down");
        }
        boolean interrupted = false;
        while (true) {
            try {
                this.queue.put(r);
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
        if (this.shutdown && this.queue.remove(r)) {
            throw new RejectedExecutionException("Executor is shutting down");
        }
        if (this.runnerPermit.tryAcquire()) {
            this.executor.submit(new Runnable() { // from class: org.ehcache.impl.internal.executor.PartitionedUnorderedExecutor.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        PartitionedUnorderedExecutor.this.liveThreads.add(Thread.currentThread());
                        try {
                            ((Runnable) PartitionedUnorderedExecutor.this.queue.remove()).run();
                            PartitionedUnorderedExecutor.this.liveThreads.remove(Thread.currentThread());
                            if (PartitionedUnorderedExecutor.this.queue.isEmpty()) {
                                PartitionedUnorderedExecutor.this.runnerPermit.release();
                                if (!PartitionedUnorderedExecutor.this.queue.isEmpty() && PartitionedUnorderedExecutor.this.runnerPermit.tryAcquire()) {
                                    PartitionedUnorderedExecutor.this.executor.submit(this);
                                    return;
                                } else {
                                    if (PartitionedUnorderedExecutor.this.isTerminated()) {
                                        PartitionedUnorderedExecutor.this.termination.countDown();
                                        return;
                                    }
                                    return;
                                }
                            }
                            PartitionedUnorderedExecutor.this.executor.submit(this);
                        } catch (Throwable th2) {
                            PartitionedUnorderedExecutor.this.liveThreads.remove(Thread.currentThread());
                            throw th2;
                        }
                    } catch (Throwable th3) {
                        if (PartitionedUnorderedExecutor.this.queue.isEmpty()) {
                            PartitionedUnorderedExecutor.this.runnerPermit.release();
                            if (!PartitionedUnorderedExecutor.this.queue.isEmpty() && PartitionedUnorderedExecutor.this.runnerPermit.tryAcquire()) {
                                PartitionedUnorderedExecutor.this.executor.submit(this);
                            } else if (PartitionedUnorderedExecutor.this.isTerminated()) {
                                PartitionedUnorderedExecutor.this.termination.countDown();
                            }
                        } else {
                            PartitionedUnorderedExecutor.this.executor.submit(this);
                        }
                        throw th3;
                    }
                }
            });
        }
    }
}
