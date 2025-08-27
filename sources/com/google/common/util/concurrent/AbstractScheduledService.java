package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;
import org.aspectj.lang.JoinPoint;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/AbstractScheduledService.class */
public abstract class AbstractScheduledService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
    private final AbstractService delegate = new AbstractService() { // from class: com.google.common.util.concurrent.AbstractScheduledService.1
        private volatile Future<?> runningTask;
        private volatile ScheduledExecutorService executorService;
        private final ReentrantLock lock = new ReentrantLock();
        private final Runnable task = new Runnable() { // from class: com.google.common.util.concurrent.AbstractScheduledService.1.1
            @Override // java.lang.Runnable
            public void run() {
                RuntimeException runtimeExceptionPropagate;
                AnonymousClass1.this.lock.lock();
                try {
                    try {
                        AbstractScheduledService.this.runOneIteration();
                        AnonymousClass1.this.lock.unlock();
                    } finally {
                    }
                } catch (Throwable th) {
                    AnonymousClass1.this.lock.unlock();
                    throw th;
                }
            }
        };

        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStart() {
            this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this.executor(), new Supplier<String>() { // from class: com.google.common.util.concurrent.AbstractScheduledService.1.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.google.common.base.Supplier
                public String get() {
                    String strValueOf = String.valueOf(String.valueOf(AbstractScheduledService.this.serviceName()));
                    String strValueOf2 = String.valueOf(String.valueOf(state()));
                    return new StringBuilder(1 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(SymbolConstants.SPACE_SYMBOL).append(strValueOf2).toString();
                }
            });
            this.executorService.execute(new Runnable() { // from class: com.google.common.util.concurrent.AbstractScheduledService.1.3
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass1.this.lock.lock();
                    try {
                        try {
                            AbstractScheduledService.this.startUp();
                            AnonymousClass1.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, AnonymousClass1.this.executorService, AnonymousClass1.this.task);
                            notifyStarted();
                            AnonymousClass1.this.lock.unlock();
                        } catch (Throwable t) {
                            notifyFailed(t);
                            throw Throwables.propagate(t);
                        }
                    } catch (Throwable th) {
                        AnonymousClass1.this.lock.unlock();
                        throw th;
                    }
                }
            });
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStop() {
            this.runningTask.cancel(false);
            this.executorService.execute(new Runnable() { // from class: com.google.common.util.concurrent.AbstractScheduledService.1.4
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        AnonymousClass1.this.lock.lock();
                        try {
                            if (state() != Service.State.STOPPING) {
                                return;
                            }
                            AbstractScheduledService.this.shutDown();
                            AnonymousClass1.this.lock.unlock();
                            notifyStopped();
                        } finally {
                            AnonymousClass1.this.lock.unlock();
                        }
                    } catch (Throwable t) {
                        notifyFailed(t);
                        throw Throwables.propagate(t);
                    }
                }
            });
        }
    };

    protected abstract void runOneIteration() throws Exception;

    protected abstract Scheduler scheduler();

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/AbstractScheduledService$Scheduler.class */
    public static abstract class Scheduler {
        abstract Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable);

        public static Scheduler newFixedDelaySchedule(final long initialDelay, final long delay, final TimeUnit unit) {
            return new Scheduler() { // from class: com.google.common.util.concurrent.AbstractScheduledService.Scheduler.1
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // com.google.common.util.concurrent.AbstractScheduledService.Scheduler
                public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
                    return executor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
                }
            };
        }

        public static Scheduler newFixedRateSchedule(final long initialDelay, final long period, final TimeUnit unit) {
            return new Scheduler() { // from class: com.google.common.util.concurrent.AbstractScheduledService.Scheduler.2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // com.google.common.util.concurrent.AbstractScheduledService.Scheduler
                public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
                    return executor.scheduleAtFixedRate(task, initialDelay, period, unit);
                }
            };
        }

        private Scheduler() {
        }
    }

    protected AbstractScheduledService() {
    }

    protected void startUp() throws Exception {
    }

    protected void shutDown() throws Exception {
    }

    protected ScheduledExecutorService executor() {
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() { // from class: com.google.common.util.concurrent.AbstractScheduledService.2
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
            }
        });
        addListener(new Service.Listener() { // from class: com.google.common.util.concurrent.AbstractScheduledService.3
            @Override // com.google.common.util.concurrent.Service.Listener
            public void terminated(Service.State from) {
                executor.shutdown();
            }

            @Override // com.google.common.util.concurrent.Service.Listener
            public void failed(Service.State from, Throwable failure) {
                executor.shutdown();
            }
        }, MoreExecutors.directExecutor());
        return executor;
    }

    protected String serviceName() {
        return getClass().getSimpleName();
    }

    public String toString() {
        String strValueOf = String.valueOf(String.valueOf(serviceName()));
        String strValueOf2 = String.valueOf(String.valueOf(state()));
        return new StringBuilder(3 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(" [").append(strValueOf2).append("]").toString();
    }

    @Override // com.google.common.util.concurrent.Service
    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service.State state() {
        return this.delegate.state();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void addListener(Service.Listener listener, Executor executor) {
        this.delegate.addListener(listener, executor);
    }

    @Override // com.google.common.util.concurrent.Service
    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service stopAsync() {
        this.delegate.stopAsync();
        return this;
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning() {
        this.delegate.awaitRunning();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
        this.delegate.awaitRunning(timeout, unit);
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
        this.delegate.awaitTerminated(timeout, unit);
    }

    @Beta
    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/AbstractScheduledService$CustomScheduler.class */
    public static abstract class CustomScheduler extends Scheduler {
        protected abstract Schedule getNextSchedule() throws Exception;

        public CustomScheduler() {
            super();
        }

        /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/AbstractScheduledService$CustomScheduler$ReschedulableCallable.class */
        private class ReschedulableCallable extends ForwardingFuture<Void> implements Callable<Void> {
            private final Runnable wrappedRunnable;
            private final ScheduledExecutorService executor;
            private final AbstractService service;
            private final ReentrantLock lock = new ReentrantLock();

            @GuardedBy(JoinPoint.SYNCHRONIZATION_LOCK)
            private Future<Void> currentFuture;

            ReschedulableCallable(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
                this.wrappedRunnable = runnable;
                this.executor = executor;
                this.service = service;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                this.wrappedRunnable.run();
                reschedule();
                return null;
            }

            public void reschedule() {
                this.lock.lock();
                try {
                    try {
                        if (this.currentFuture == null || !this.currentFuture.isCancelled()) {
                            Schedule schedule = CustomScheduler.this.getNextSchedule();
                            this.currentFuture = this.executor.schedule(this, schedule.delay, schedule.unit);
                        }
                        this.lock.unlock();
                    } catch (Throwable e) {
                        this.service.notifyFailed(e);
                        this.lock.unlock();
                    }
                } catch (Throwable th) {
                    this.lock.unlock();
                    throw th;
                }
            }

            @Override // com.google.common.util.concurrent.ForwardingFuture, java.util.concurrent.Future
            public boolean cancel(boolean mayInterruptIfRunning) {
                this.lock.lock();
                try {
                    boolean zCancel = this.currentFuture.cancel(mayInterruptIfRunning);
                    this.lock.unlock();
                    return zCancel;
                } catch (Throwable th) {
                    this.lock.unlock();
                    throw th;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
            public Future<Void> delegate() {
                throw new UnsupportedOperationException("Only cancel is supported by this future");
            }
        }

        @Override // com.google.common.util.concurrent.AbstractScheduledService.Scheduler
        final Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
            ReschedulableCallable task = new ReschedulableCallable(service, executor, runnable);
            task.reschedule();
            return task;
        }

        @Beta
        /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/AbstractScheduledService$CustomScheduler$Schedule.class */
        protected static final class Schedule {
            private final long delay;
            private final TimeUnit unit;

            public Schedule(long delay, TimeUnit unit) {
                this.delay = delay;
                this.unit = (TimeUnit) Preconditions.checkNotNull(unit);
            }
        }
    }
}
