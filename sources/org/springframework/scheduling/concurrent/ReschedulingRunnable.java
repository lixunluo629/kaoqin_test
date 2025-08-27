package org.springframework.scheduling.concurrent;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.util.ErrorHandler;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/concurrent/ReschedulingRunnable.class */
class ReschedulingRunnable extends DelegatingErrorHandlingRunnable implements ScheduledFuture<Object> {
    private final Trigger trigger;
    private final SimpleTriggerContext triggerContext;
    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> currentFuture;
    private Date scheduledExecutionTime;
    private final Object triggerContextMonitor;

    public ReschedulingRunnable(Runnable delegate, Trigger trigger, ScheduledExecutorService executor, ErrorHandler errorHandler) {
        super(delegate, errorHandler);
        this.triggerContext = new SimpleTriggerContext();
        this.triggerContextMonitor = new Object();
        this.trigger = trigger;
        this.executor = executor;
    }

    public ScheduledFuture<?> schedule() {
        synchronized (this.triggerContextMonitor) {
            this.scheduledExecutionTime = this.trigger.nextExecutionTime(this.triggerContext);
            if (this.scheduledExecutionTime == null) {
                return null;
            }
            long initialDelay = this.scheduledExecutionTime.getTime() - System.currentTimeMillis();
            this.currentFuture = this.executor.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
            return this;
        }
    }

    @Override // org.springframework.scheduling.support.DelegatingErrorHandlingRunnable, java.lang.Runnable
    public void run() {
        Date actualExecutionTime = new Date();
        super.run();
        Date completionTime = new Date();
        synchronized (this.triggerContextMonitor) {
            this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
            if (!this.currentFuture.isCancelled()) {
                schedule();
            }
        }
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean zCancel;
        synchronized (this.triggerContextMonitor) {
            zCancel = this.currentFuture.cancel(mayInterruptIfRunning);
        }
        return zCancel;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        boolean zIsCancelled;
        synchronized (this.triggerContextMonitor) {
            zIsCancelled = this.currentFuture.isCancelled();
        }
        return zIsCancelled;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        boolean zIsDone;
        synchronized (this.triggerContextMonitor) {
            zIsDone = this.currentFuture.isDone();
        }
        return zIsDone;
    }

    @Override // java.util.concurrent.Future
    public Object get() throws ExecutionException, InterruptedException {
        ScheduledFuture<?> curr;
        synchronized (this.triggerContextMonitor) {
            curr = this.currentFuture;
        }
        return curr.get();
    }

    @Override // java.util.concurrent.Future
    public Object get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        ScheduledFuture<?> curr;
        synchronized (this.triggerContextMonitor) {
            curr = this.currentFuture;
        }
        return curr.get(timeout, unit);
    }

    @Override // java.util.concurrent.Delayed
    public long getDelay(TimeUnit unit) {
        ScheduledFuture<?> curr;
        synchronized (this.triggerContextMonitor) {
            curr = this.currentFuture;
        }
        return curr.getDelay(unit);
    }

    @Override // java.lang.Comparable
    public int compareTo(Delayed other) {
        if (this == other) {
            return 0;
        }
        long diff = getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
        if (diff == 0) {
            return 0;
        }
        return diff < 0 ? -1 : 1;
    }
}
