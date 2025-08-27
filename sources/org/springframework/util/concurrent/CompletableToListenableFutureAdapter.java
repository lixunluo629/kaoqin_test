package org.springframework.util.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import org.springframework.lang.UsesJava8;

@UsesJava8
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/concurrent/CompletableToListenableFutureAdapter.class */
public class CompletableToListenableFutureAdapter<T> implements ListenableFuture<T> {
    private final CompletableFuture<T> completableFuture;
    private final ListenableFutureCallbackRegistry<T> callbacks;

    public CompletableToListenableFutureAdapter(CompletionStage<T> completionStage) {
        this((CompletableFuture) completionStage.toCompletableFuture());
    }

    public CompletableToListenableFutureAdapter(CompletableFuture<T> completableFuture) {
        this.callbacks = new ListenableFutureCallbackRegistry<>();
        this.completableFuture = completableFuture;
        this.completableFuture.handle((BiFunction) new BiFunction<T, Throwable, Object>() { // from class: org.springframework.util.concurrent.CompletableToListenableFutureAdapter.1
            @Override // java.util.function.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object obj, Throwable th) {
                return apply2((AnonymousClass1) obj, th);
            }

            /* renamed from: apply, reason: avoid collision after fix types in other method */
            public Object apply2(T result, Throwable ex) {
                if (ex != null) {
                    CompletableToListenableFutureAdapter.this.callbacks.failure(ex);
                    return null;
                }
                CompletableToListenableFutureAdapter.this.callbacks.success(result);
                return null;
            }
        });
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

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.completableFuture.cancel(mayInterruptIfRunning);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.completableFuture.isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.completableFuture.isDone();
    }

    @Override // java.util.concurrent.Future
    public T get() throws ExecutionException, InterruptedException {
        return this.completableFuture.get();
    }

    @Override // java.util.concurrent.Future
    public T get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return this.completableFuture.get(timeout, unit);
    }
}
