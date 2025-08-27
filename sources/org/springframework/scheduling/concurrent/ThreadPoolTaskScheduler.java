package org.springframework.scheduling.concurrent;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.lang.UsesJava7;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.TaskUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ErrorHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/concurrent/ThreadPoolTaskScheduler.class */
public class ThreadPoolTaskScheduler extends ExecutorConfigurationSupport implements AsyncListenableTaskExecutor, SchedulingTaskExecutor, TaskScheduler {
    private static final boolean setRemoveOnCancelPolicyAvailable = ClassUtils.hasMethod(ScheduledThreadPoolExecutor.class, "setRemoveOnCancelPolicy", Boolean.TYPE);
    private volatile int poolSize = 1;
    private volatile boolean removeOnCancelPolicy = false;
    private volatile ErrorHandler errorHandler;
    private volatile ScheduledExecutorService scheduledExecutor;

    public void setPoolSize(int poolSize) {
        Assert.isTrue(poolSize > 0, "'poolSize' must be 1 or higher");
        this.poolSize = poolSize;
        if (this.scheduledExecutor instanceof ScheduledThreadPoolExecutor) {
            ((ScheduledThreadPoolExecutor) this.scheduledExecutor).setCorePoolSize(poolSize);
        }
    }

    @UsesJava7
    public void setRemoveOnCancelPolicy(boolean removeOnCancelPolicy) {
        this.removeOnCancelPolicy = removeOnCancelPolicy;
        if (setRemoveOnCancelPolicyAvailable && (this.scheduledExecutor instanceof ScheduledThreadPoolExecutor)) {
            ((ScheduledThreadPoolExecutor) this.scheduledExecutor).setRemoveOnCancelPolicy(removeOnCancelPolicy);
        } else if (removeOnCancelPolicy && this.scheduledExecutor != null) {
            this.logger.info("Could not apply remove-on-cancel policy - not a Java 7+ ScheduledThreadPoolExecutor");
        }
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override // org.springframework.scheduling.concurrent.ExecutorConfigurationSupport
    @UsesJava7
    protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        this.scheduledExecutor = createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);
        if (this.removeOnCancelPolicy) {
            if (setRemoveOnCancelPolicyAvailable && (this.scheduledExecutor instanceof ScheduledThreadPoolExecutor)) {
                ((ScheduledThreadPoolExecutor) this.scheduledExecutor).setRemoveOnCancelPolicy(true);
            } else {
                this.logger.info("Could not apply remove-on-cancel policy - not a Java 7+ ScheduledThreadPoolExecutor");
            }
        }
        return this.scheduledExecutor;
    }

    protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
    }

    public ScheduledExecutorService getScheduledExecutor() throws IllegalStateException {
        Assert.state(this.scheduledExecutor != null, "ThreadPoolTaskScheduler not initialized");
        return this.scheduledExecutor;
    }

    public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() throws IllegalStateException {
        Assert.state(this.scheduledExecutor instanceof ScheduledThreadPoolExecutor, "No ScheduledThreadPoolExecutor available");
        return (ScheduledThreadPoolExecutor) this.scheduledExecutor;
    }

    public int getPoolSize() {
        if (this.scheduledExecutor == null) {
            return this.poolSize;
        }
        return getScheduledThreadPoolExecutor().getPoolSize();
    }

    @UsesJava7
    public boolean isRemoveOnCancelPolicy() {
        if (!setRemoveOnCancelPolicyAvailable) {
            return false;
        }
        if (this.scheduledExecutor == null) {
            return this.removeOnCancelPolicy;
        }
        return getScheduledThreadPoolExecutor().getRemoveOnCancelPolicy();
    }

    public int getActiveCount() {
        if (this.scheduledExecutor == null) {
            return 0;
        }
        return getScheduledThreadPoolExecutor().getActiveCount();
    }

    @Override // org.springframework.core.task.TaskExecutor, java.util.concurrent.Executor
    public void execute(Runnable task) throws IllegalStateException {
        Executor executor = getScheduledExecutor();
        try {
            executor.execute(errorHandlingTask(task, false));
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    @Override // org.springframework.core.task.AsyncTaskExecutor
    public void execute(Runnable task, long startTimeout) throws IllegalStateException {
        execute(task);
    }

    @Override // org.springframework.core.task.AsyncTaskExecutor
    public Future<?> submit(Runnable task) throws IllegalStateException {
        ExecutorService executor = getScheduledExecutor();
        try {
            return executor.submit(errorHandlingTask(task, false));
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    @Override // org.springframework.core.task.AsyncTaskExecutor
    public <T> Future<T> submit(Callable<T> task) throws IllegalStateException {
        ExecutorService executor = getScheduledExecutor();
        try {
            Callable<T> taskToUse = task;
            if (this.errorHandler != null) {
                taskToUse = new DelegatingErrorHandlingCallable(task, this.errorHandler);
            }
            return executor.submit(taskToUse);
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    @Override // org.springframework.core.task.AsyncListenableTaskExecutor
    public ListenableFuture<?> submitListenable(Runnable task) throws IllegalStateException {
        ExecutorService executor = getScheduledExecutor();
        try {
            ListenableFutureTask<Object> future = new ListenableFutureTask<>(task, null);
            executor.execute(errorHandlingTask(future, false));
            return future;
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    @Override // org.springframework.core.task.AsyncListenableTaskExecutor
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) throws IllegalStateException {
        ExecutorService executor = getScheduledExecutor();
        try {
            ListenableFutureTask<T> future = new ListenableFutureTask<>(task);
            executor.execute(errorHandlingTask(future, false));
            return future;
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    @Override // org.springframework.scheduling.SchedulingTaskExecutor
    public boolean prefersShortLivedTasks() {
        return true;
    }

    @Override // org.springframework.scheduling.TaskScheduler
    public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) throws IllegalStateException {
        ScheduledExecutorService executor = getScheduledExecutor();
        try {
            ErrorHandler errorHandler = this.errorHandler != null ? this.errorHandler : TaskUtils.getDefaultErrorHandler(true);
            return new ReschedulingRunnable(task, trigger, executor, errorHandler).schedule();
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    @Override // org.springframework.scheduling.TaskScheduler
    public ScheduledFuture<?> schedule(Runnable task, Date startTime) throws IllegalStateException {
        ScheduledExecutorService executor = getScheduledExecutor();
        long initialDelay = startTime.getTime() - System.currentTimeMillis();
        try {
            return executor.schedule(errorHandlingTask(task, false), initialDelay, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    @Override // org.springframework.scheduling.TaskScheduler
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) throws IllegalStateException {
        ScheduledExecutorService executor = getScheduledExecutor();
        long initialDelay = startTime.getTime() - System.currentTimeMillis();
        try {
            return executor.scheduleAtFixedRate(errorHandlingTask(task, true), initialDelay, period, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    @Override // org.springframework.scheduling.TaskScheduler
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) throws IllegalStateException {
        ScheduledExecutorService executor = getScheduledExecutor();
        try {
            return executor.scheduleAtFixedRate(errorHandlingTask(task, true), 0L, period, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    @Override // org.springframework.scheduling.TaskScheduler
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) throws IllegalStateException {
        ScheduledExecutorService executor = getScheduledExecutor();
        long initialDelay = startTime.getTime() - System.currentTimeMillis();
        try {
            return executor.scheduleWithFixedDelay(errorHandlingTask(task, true), initialDelay, delay, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    @Override // org.springframework.scheduling.TaskScheduler
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) throws IllegalStateException {
        ScheduledExecutorService executor = getScheduledExecutor();
        try {
            return executor.scheduleWithFixedDelay(errorHandlingTask(task, true), 0L, delay, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
    }

    private Runnable errorHandlingTask(Runnable task, boolean isRepeatingTask) {
        return TaskUtils.decorateTaskWithErrorHandler(task, this.errorHandler, isRepeatingTask);
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/concurrent/ThreadPoolTaskScheduler$DelegatingErrorHandlingCallable.class */
    private static class DelegatingErrorHandlingCallable<V> implements Callable<V> {
        private final Callable<V> delegate;
        private final ErrorHandler errorHandler;

        public DelegatingErrorHandlingCallable(Callable<V> delegate, ErrorHandler errorHandler) {
            this.delegate = delegate;
            this.errorHandler = errorHandler;
        }

        @Override // java.util.concurrent.Callable
        public V call() throws Exception {
            try {
                return this.delegate.call();
            } catch (Throwable t) {
                this.errorHandler.handleError(t);
                return null;
            }
        }
    }
}
