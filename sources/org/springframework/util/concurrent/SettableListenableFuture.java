package org.springframework.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.util.Assert;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/concurrent/SettableListenableFuture.class */
public class SettableListenableFuture<T> implements ListenableFuture<T> {
    private static final Callable<Object> DUMMY_CALLABLE = new Callable<Object>() { // from class: org.springframework.util.concurrent.SettableListenableFuture.1
        @Override // java.util.concurrent.Callable
        public Object call() throws Exception {
            throw new IllegalStateException("Should never be called");
        }
    };
    private final SettableTask<T> settableTask = new SettableTask<>();

    public boolean set(T value) {
        return this.settableTask.setResultValue(value);
    }

    public boolean setException(Throwable exception) {
        Assert.notNull(exception, "Exception must not be null");
        return this.settableTask.setExceptionResult(exception);
    }

    @Override // org.springframework.util.concurrent.ListenableFuture
    public void addCallback(ListenableFutureCallback<? super T> callback) {
        this.settableTask.addCallback(callback);
    }

    @Override // org.springframework.util.concurrent.ListenableFuture
    public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {
        this.settableTask.addCallback(successCallback, failureCallback);
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean cancelled = this.settableTask.cancel(mayInterruptIfRunning);
        if (cancelled && mayInterruptIfRunning) {
            interruptTask();
        }
        return cancelled;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.settableTask.isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.settableTask.isDone();
    }

    @Override // java.util.concurrent.Future
    public T get() throws ExecutionException, InterruptedException {
        return (T) this.settableTask.get();
    }

    @Override // java.util.concurrent.Future
    public T get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return (T) this.settableTask.get(j, timeUnit);
    }

    protected void interruptTask() {
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/concurrent/SettableListenableFuture$SettableTask.class */
    private static class SettableTask<T> extends ListenableFutureTask<T> {
        private volatile Thread completingThread;

        public SettableTask() {
            super(SettableListenableFuture.DUMMY_CALLABLE);
        }

        public boolean setResultValue(T value) {
            set(value);
            return checkCompletingThread();
        }

        public boolean setExceptionResult(Throwable exception) {
            setException(exception);
            return checkCompletingThread();
        }

        @Override // org.springframework.util.concurrent.ListenableFutureTask, java.util.concurrent.FutureTask
        protected void done() {
            if (!isCancelled()) {
                this.completingThread = Thread.currentThread();
            }
            super.done();
        }

        private boolean checkCompletingThread() {
            boolean check = this.completingThread == Thread.currentThread();
            if (check) {
                this.completingThread = null;
            }
            return check;
        }
    }
}
