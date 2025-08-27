package org.springframework.scheduling.annotation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/annotation/AsyncResult.class */
public class AsyncResult<V> implements ListenableFuture<V> {
    private final V value;
    private final ExecutionException executionException;

    public AsyncResult(V value) {
        this(value, null);
    }

    private AsyncResult(V value, ExecutionException ex) {
        this.value = value;
        this.executionException = ex;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return true;
    }

    @Override // java.util.concurrent.Future
    public V get() throws ExecutionException {
        if (this.executionException != null) {
            throw this.executionException;
        }
        return this.value;
    }

    @Override // java.util.concurrent.Future
    public V get(long timeout, TimeUnit unit) throws ExecutionException {
        return get();
    }

    @Override // org.springframework.util.concurrent.ListenableFuture
    public void addCallback(ListenableFutureCallback<? super V> callback) {
        addCallback(callback, callback);
    }

    @Override // org.springframework.util.concurrent.ListenableFuture
    public void addCallback(SuccessCallback<? super V> successCallback, FailureCallback failureCallback) {
        try {
            if (this.executionException != null) {
                Throwable cause = this.executionException.getCause();
                failureCallback.onFailure(cause != null ? cause : this.executionException);
            } else {
                successCallback.onSuccess(this.value);
            }
        } catch (Throwable th) {
        }
    }

    public static <V> ListenableFuture<V> forValue(V value) {
        return new AsyncResult(value, null);
    }

    public static <V> ListenableFuture<V> forExecutionException(Throwable ex) {
        return new AsyncResult(null, ex instanceof ExecutionException ? (ExecutionException) ex : new ExecutionException(ex));
    }
}
