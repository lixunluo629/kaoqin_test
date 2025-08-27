package org.ehcache.impl.internal.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.ehcache.impl.internal.executor.OutOfBandScheduledExecutor;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/PartitionedScheduledExecutor.class */
class PartitionedScheduledExecutor extends AbstractExecutorService implements ScheduledExecutorService {
    private final OutOfBandScheduledExecutor scheduler;
    private final ExecutorService worker;
    private volatile boolean shutdown;
    private volatile Future<List<Runnable>> termination;

    PartitionedScheduledExecutor(OutOfBandScheduledExecutor scheduler, ExecutorService worker) {
        this.scheduler = scheduler;
        this.worker = worker;
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        if (this.shutdown) {
            throw new RejectedExecutionException();
        }
        ScheduledFuture<?> scheduled = this.scheduler.schedule(this.worker, command, delay, unit);
        if (this.shutdown && scheduled.cancel(false)) {
            throw new RejectedExecutionException();
        }
        return scheduled;
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        if (this.shutdown) {
            throw new RejectedExecutionException();
        }
        ScheduledFuture<V> scheduled = this.scheduler.schedule(this.worker, callable, delay, unit);
        if (this.shutdown && scheduled.cancel(false)) {
            throw new RejectedExecutionException();
        }
        return scheduled;
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        if (this.shutdown) {
            throw new RejectedExecutionException();
        }
        ScheduledFuture<?> scheduled = this.scheduler.scheduleAtFixedRate(this.worker, command, initialDelay, period, unit);
        if (this.shutdown && scheduled.cancel(false)) {
            throw new RejectedExecutionException();
        }
        return scheduled;
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        if (this.shutdown) {
            throw new RejectedExecutionException();
        }
        ScheduledFuture<?> scheduled = this.scheduler.scheduleWithFixedDelay(this.worker, command, initialDelay, delay, unit);
        if (this.shutdown && scheduled.cancel(false)) {
            throw new RejectedExecutionException();
        }
        return scheduled;
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        this.shutdown = true;
        try {
            Long longestDelay = (Long) ExecutorUtil.waitFor(this.scheduler.schedule((ExecutorService) null, new Callable<Long>() { // from class: org.ehcache.impl.internal.executor.PartitionedScheduledExecutor.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Long call() throws ExecutionException {
                    long maxDelay = 0;
                    Iterator<Runnable> it = PartitionedScheduledExecutor.this.scheduler.getQueue().iterator();
                    while (it.hasNext()) {
                        Runnable job = it.next();
                        if (job instanceof OutOfBandScheduledExecutor.OutOfBandRsf) {
                            OutOfBandScheduledExecutor.OutOfBandRsf<?> oobJob = (OutOfBandScheduledExecutor.OutOfBandRsf) job;
                            if (oobJob.getExecutor() == PartitionedScheduledExecutor.this.worker) {
                                if (oobJob.isPeriodic()) {
                                    oobJob.cancel(false);
                                    it.remove();
                                } else {
                                    maxDelay = Math.max(maxDelay, oobJob.getDelay(TimeUnit.NANOSECONDS));
                                }
                            }
                        }
                    }
                    return Long.valueOf(maxDelay);
                }
            }, 0L, TimeUnit.NANOSECONDS));
            this.termination = this.scheduler.schedule(this.worker, new Callable<List<Runnable>>() { // from class: org.ehcache.impl.internal.executor.PartitionedScheduledExecutor.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public List<Runnable> call() {
                    PartitionedScheduledExecutor.this.worker.shutdown();
                    return Collections.emptyList();
                }
            }, longestDelay.longValue() + 1, TimeUnit.NANOSECONDS);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        this.shutdown = true;
        try {
            this.termination = this.scheduler.schedule((ExecutorService) null, new Callable<List<Runnable>>() { // from class: org.ehcache.impl.internal.executor.PartitionedScheduledExecutor.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public List<Runnable> call() throws Exception {
                    List<Runnable> abortedTasks = new ArrayList<>();
                    Iterator<Runnable> it = PartitionedScheduledExecutor.this.scheduler.getQueue().iterator();
                    while (it.hasNext()) {
                        Runnable job = it.next();
                        if (job instanceof OutOfBandScheduledExecutor.OutOfBandRsf) {
                            OutOfBandScheduledExecutor.OutOfBandRsf<?> oobJob = (OutOfBandScheduledExecutor.OutOfBandRsf) job;
                            if (oobJob.getExecutor() == PartitionedScheduledExecutor.this.worker) {
                                abortedTasks.add(job);
                                it.remove();
                            }
                        }
                    }
                    abortedTasks.addAll(PartitionedScheduledExecutor.this.worker.shutdownNow());
                    return abortedTasks;
                }
            }, 0L, TimeUnit.NANOSECONDS);
            return (List) ExecutorUtil.waitFor(this.termination);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return this.shutdown;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return isShutdown() && this.termination.isDone() && this.worker.isTerminated();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long time, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        if (isShutdown()) {
            if (this.termination.isDone()) {
                return this.worker.awaitTermination(time, unit);
            }
            long end = System.nanoTime() + unit.toNanos(time);
            try {
                this.termination.get(time, unit);
                return this.worker.awaitTermination(end - System.nanoTime(), TimeUnit.NANOSECONDS);
            } catch (ExecutionException e) {
                throw new RuntimeException(e.getCause());
            } catch (TimeoutException e2) {
                return false;
            }
        }
        return false;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        schedule(runnable, 0L, TimeUnit.NANOSECONDS);
    }
}
