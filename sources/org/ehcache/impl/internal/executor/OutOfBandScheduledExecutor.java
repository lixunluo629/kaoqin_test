package org.ehcache.impl.internal.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.ehcache.impl.internal.util.ThreadFactoryUtil;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/OutOfBandScheduledExecutor.class */
class OutOfBandScheduledExecutor {
    private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1, ThreadFactoryUtil.threadFactory("scheduled")) { // from class: org.ehcache.impl.internal.executor.OutOfBandScheduledExecutor.1
        @Override // java.util.concurrent.ScheduledThreadPoolExecutor
        protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> clbl, RunnableScheduledFuture<V> rsf) {
            return new OutOfBandRsf(((ExecutorCarrier) clbl).executor(), rsf);
        }

        @Override // java.util.concurrent.ScheduledThreadPoolExecutor
        protected <V> RunnableScheduledFuture<V> decorateTask(Runnable r, RunnableScheduledFuture<V> rsf) {
            return new OutOfBandRsf(((ExecutorCarrier) r).executor(), rsf);
        }
    };

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/OutOfBandScheduledExecutor$ExecutorCarrier.class */
    interface ExecutorCarrier {
        ExecutorService executor();
    }

    OutOfBandScheduledExecutor() {
    }

    public BlockingQueue<Runnable> getQueue() {
        return this.scheduler.getQueue();
    }

    public ScheduledFuture<?> schedule(ExecutorService using, Runnable command, long delay, TimeUnit unit) {
        return this.scheduler.schedule(new ExecutorCarryingRunnable(using, command), delay, unit);
    }

    public <V> ScheduledFuture<V> schedule(ExecutorService using, Callable<V> callable, long delay, TimeUnit unit) {
        return this.scheduler.schedule(new ExecutorCarryingCallable(using, callable), delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(ExecutorService using, Runnable command, long initialDelay, long period, TimeUnit unit) {
        return this.scheduler.scheduleAtFixedRate(new ExecutorCarryingRunnable(using, command), initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(ExecutorService using, Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return this.scheduler.scheduleWithFixedDelay(new ExecutorCarryingRunnable(using, command), initialDelay, delay, unit);
    }

    public void shutdownNow() {
        this.scheduler.shutdownNow();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.scheduler.awaitTermination(timeout, unit);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/OutOfBandScheduledExecutor$ExecutorCarryingRunnable.class */
    static class ExecutorCarryingRunnable implements ExecutorCarrier, Runnable {
        private final ExecutorService executor;
        private final Runnable runnable;

        public ExecutorCarryingRunnable(ExecutorService executor, Runnable runnable) {
            this.executor = executor;
            this.runnable = runnable;
        }

        @Override // org.ehcache.impl.internal.executor.OutOfBandScheduledExecutor.ExecutorCarrier
        public ExecutorService executor() {
            return this.executor;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.runnable.run();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/OutOfBandScheduledExecutor$ExecutorCarryingCallable.class */
    static class ExecutorCarryingCallable<T> implements ExecutorCarrier, Callable<T> {
        private final ExecutorService executor;
        private final Callable<T> callable;

        public ExecutorCarryingCallable(ExecutorService executor, Callable<T> callable) {
            this.executor = executor;
            this.callable = callable;
        }

        @Override // org.ehcache.impl.internal.executor.OutOfBandScheduledExecutor.ExecutorCarrier
        public ExecutorService executor() {
            return this.executor;
        }

        @Override // java.util.concurrent.Callable
        public T call() throws Exception {
            return this.callable.call();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/OutOfBandScheduledExecutor$OutOfBandRsf.class */
    static class OutOfBandRsf<T> implements RunnableScheduledFuture<T> {
        private final ExecutorService worker;
        private final RunnableScheduledFuture<T> delegate;
        private volatile Future<?> execution;

        OutOfBandRsf(ExecutorService worker, RunnableScheduledFuture<T> original) {
            this.worker = worker;
            this.delegate = original;
        }

        public ExecutorService getExecutor() {
            return this.worker;
        }

        @Override // java.util.concurrent.RunnableScheduledFuture
        public boolean isPeriodic() {
            return this.delegate.isPeriodic();
        }

        @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
        public synchronized void run() {
            if (this.worker == null || this.worker.isShutdown()) {
                this.delegate.run();
            } else {
                this.execution = this.worker.submit(this.delegate);
            }
        }

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean interrupt) {
            Future<?> currentExecution = this.execution;
            return (currentExecution == null || currentExecution.cancel(interrupt)) && this.delegate.cancel(interrupt);
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return this.delegate.isCancelled();
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return this.delegate.isDone();
        }

        @Override // java.util.concurrent.Future
        public T get() throws ExecutionException, InterruptedException {
            return (T) this.delegate.get();
        }

        @Override // java.util.concurrent.Future
        public T get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
            return (T) this.delegate.get(j, timeUnit);
        }

        @Override // java.util.concurrent.Delayed
        public long getDelay(TimeUnit tu) {
            return this.delegate.getDelay(tu);
        }

        @Override // java.lang.Comparable
        public int compareTo(Delayed t) {
            return this.delegate.compareTo(t);
        }

        public int hashCode() {
            return this.delegate.hashCode();
        }

        public boolean equals(Object obj) {
            return this.delegate.equals(obj);
        }
    }
}
