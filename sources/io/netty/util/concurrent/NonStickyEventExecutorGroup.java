package io.netty.util.concurrent;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/NonStickyEventExecutorGroup.class */
public final class NonStickyEventExecutorGroup implements EventExecutorGroup {
    private final EventExecutorGroup group;
    private final int maxTaskExecutePerRun;

    @Override // java.util.concurrent.ExecutorService
    public /* bridge */ /* synthetic */ java.util.concurrent.Future submit(Runnable runnable, Object obj) {
        return submit(runnable, (Runnable) obj);
    }

    public NonStickyEventExecutorGroup(EventExecutorGroup group) {
        this(group, 1024);
    }

    public NonStickyEventExecutorGroup(EventExecutorGroup group, int maxTaskExecutePerRun) {
        this.group = verify(group);
        this.maxTaskExecutePerRun = ObjectUtil.checkPositive(maxTaskExecutePerRun, "maxTaskExecutePerRun");
    }

    private static EventExecutorGroup verify(EventExecutorGroup group) {
        for (EventExecutor executor : (EventExecutorGroup) ObjectUtil.checkNotNull(group, "group")) {
            if (executor instanceof OrderedEventExecutor) {
                throw new IllegalArgumentException("EventExecutorGroup " + group + " contains OrderedEventExecutors: " + executor);
            }
        }
        return group;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NonStickyOrderedEventExecutor newExecutor(EventExecutor executor) {
        return new NonStickyOrderedEventExecutor(executor, this.maxTaskExecutePerRun);
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public boolean isShuttingDown() {
        return this.group.isShuttingDown();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully() {
        return this.group.shutdownGracefully();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        return this.group.shutdownGracefully(quietPeriod, timeout, unit);
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> terminationFuture() {
        return this.group.terminationFuture();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, java.util.concurrent.ExecutorService
    public void shutdown() {
        this.group.shutdown();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public List<Runnable> shutdownNow() {
        return this.group.shutdownNow();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, io.netty.channel.EventLoopGroup
    public EventExecutor next() {
        return newExecutor(this.group.next());
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, java.lang.Iterable
    public Iterator<EventExecutor> iterator() {
        final Iterator<EventExecutor> itr = this.group.iterator();
        return new Iterator<EventExecutor>() { // from class: io.netty.util.concurrent.NonStickyEventExecutorGroup.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return itr.hasNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public EventExecutor next() {
                return NonStickyEventExecutorGroup.this.newExecutor((EventExecutor) itr.next());
            }

            @Override // java.util.Iterator
            public void remove() {
                itr.remove();
            }
        };
    }

    @Override // java.util.concurrent.ExecutorService
    public Future<?> submit(Runnable task) {
        return this.group.submit(task);
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup, java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Runnable task, T result) {
        return this.group.submit(task, (Runnable) result);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Callable<T> task) {
        return this.group.submit((Callable) task);
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return this.group.schedule(command, delay, unit);
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return this.group.schedule((Callable) callable, delay, unit);
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return this.group.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return this.group.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return this.group.isShutdown();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return this.group.isTerminated();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.group.awaitTermination(timeout, unit);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.group.invokeAll(tasks);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return this.group.invokeAll(tasks, timeout, unit);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws ExecutionException, InterruptedException {
        return (T) this.group.invokeAny(collection);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return (T) this.group.invokeAny(collection, j, timeUnit);
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        this.group.execute(command);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/NonStickyEventExecutorGroup$NonStickyOrderedEventExecutor.class */
    private static final class NonStickyOrderedEventExecutor extends AbstractEventExecutor implements Runnable, OrderedEventExecutor {
        private final EventExecutor executor;
        private final Queue<Runnable> tasks;
        private static final int NONE = 0;
        private static final int SUBMITTED = 1;
        private static final int RUNNING = 2;
        private final AtomicInteger state;
        private final int maxTaskExecutePerRun;

        NonStickyOrderedEventExecutor(EventExecutor executor, int maxTaskExecutePerRun) {
            super(executor);
            this.tasks = PlatformDependent.newMpscQueue();
            this.state = new AtomicInteger();
            this.executor = executor;
            this.maxTaskExecutePerRun = maxTaskExecutePerRun;
        }

        @Override // java.lang.Runnable
        public void run() {
            Runnable task;
            if (!this.state.compareAndSet(1, 2)) {
                return;
            }
            while (true) {
                int i = 0;
                while (i < this.maxTaskExecutePerRun && (task = this.tasks.poll()) != null) {
                    try {
                        safeExecute(task);
                        i++;
                    } catch (Throwable th) {
                        if (i == this.maxTaskExecutePerRun) {
                            try {
                                this.state.set(1);
                                this.executor.execute(this);
                                return;
                            } catch (Throwable th2) {
                                this.state.set(2);
                                throw th;
                            }
                        }
                        this.state.set(0);
                        if (this.tasks.isEmpty() || !this.state.compareAndSet(0, 2)) {
                            return;
                        }
                        throw th;
                    }
                }
                if (i == this.maxTaskExecutePerRun) {
                    try {
                        this.state.set(1);
                        this.executor.execute(this);
                        return;
                    } catch (Throwable th3) {
                        this.state.set(2);
                    }
                } else {
                    this.state.set(0);
                    if (this.tasks.isEmpty() || !this.state.compareAndSet(0, 2)) {
                        return;
                    }
                }
            }
        }

        @Override // io.netty.util.concurrent.EventExecutor
        public boolean inEventLoop(Thread thread) {
            return false;
        }

        @Override // io.netty.util.concurrent.AbstractEventExecutor, io.netty.util.concurrent.EventExecutor
        public boolean inEventLoop() {
            return false;
        }

        @Override // io.netty.util.concurrent.EventExecutorGroup
        public boolean isShuttingDown() {
            return this.executor.isShutdown();
        }

        @Override // io.netty.util.concurrent.EventExecutorGroup
        public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
            return this.executor.shutdownGracefully(quietPeriod, timeout, unit);
        }

        @Override // io.netty.util.concurrent.EventExecutorGroup
        public Future<?> terminationFuture() {
            return this.executor.terminationFuture();
        }

        @Override // io.netty.util.concurrent.AbstractEventExecutor, java.util.concurrent.ExecutorService, io.netty.util.concurrent.EventExecutorGroup
        public void shutdown() {
            this.executor.shutdown();
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean isShutdown() {
            return this.executor.isShutdown();
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean isTerminated() {
            return this.executor.isTerminated();
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return this.executor.awaitTermination(timeout, unit);
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable command) {
            if (!this.tasks.offer(command)) {
                throw new RejectedExecutionException();
            }
            if (this.state.compareAndSet(0, 1)) {
                try {
                    this.executor.execute(this);
                } catch (Throwable e) {
                    this.tasks.remove(command);
                    PlatformDependent.throwException(e);
                }
            }
        }
    }
}
