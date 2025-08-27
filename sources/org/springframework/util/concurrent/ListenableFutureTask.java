package org.springframework.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/concurrent/ListenableFutureTask.class */
public class ListenableFutureTask<T> extends FutureTask<T> implements ListenableFuture<T> {
    private final ListenableFutureCallbackRegistry<T> callbacks;

    public ListenableFutureTask(Callable<T> callable) {
        super(callable);
        this.callbacks = new ListenableFutureCallbackRegistry<>();
    }

    public ListenableFutureTask(Runnable runnable, T result) {
        super(runnable, result);
        this.callbacks = new ListenableFutureCallbackRegistry<>();
    }

    @Override // org.springframework.util.concurrent.ListenableFuture
    public void addCallback(ListenableFutureCallback<? super T> callback) {
        this.callbacks.addCallback(callback);
    }

    @Override // org.springframework.util.concurrent.ListenableFuture
    public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {
        this.callbacks.addSuccessCallback(successCallback);
        this.callbacks.addFailureCallback(failureCallback);
    }

    @Override // java.util.concurrent.FutureTask
    protected void done() {
        Throwable cause;
        try {
            T result = get();
            this.callbacks.success(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            cause = ex.getCause();
            if (cause == null) {
                cause = ex;
            }
            this.callbacks.failure(cause);
        } catch (Throwable ex2) {
            cause = ex2;
            this.callbacks.failure(cause);
        }
    }
}
