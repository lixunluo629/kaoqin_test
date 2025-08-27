package org.springframework.util.concurrent;

import java.util.concurrent.ExecutionException;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/concurrent/ListenableFutureAdapter.class */
public abstract class ListenableFutureAdapter<T, S> extends FutureAdapter<T, S> implements ListenableFuture<T> {
    protected ListenableFutureAdapter(ListenableFuture<S> adaptee) {
        super(adaptee);
    }

    @Override // org.springframework.util.concurrent.ListenableFuture
    public void addCallback(ListenableFutureCallback<? super T> callback) {
        addCallback(callback, callback);
    }

    @Override // org.springframework.util.concurrent.ListenableFuture
    public void addCallback(final SuccessCallback<? super T> successCallback, final FailureCallback failureCallback) {
        ListenableFuture<S> listenableAdaptee = (ListenableFuture) getAdaptee();
        listenableAdaptee.addCallback(new ListenableFutureCallback<S>() { // from class: org.springframework.util.concurrent.ListenableFutureAdapter.1
            @Override // org.springframework.util.concurrent.SuccessCallback
            public void onSuccess(S result) {
                try {
                    T adapted = ListenableFutureAdapter.this.adaptInternal(result);
                    successCallback.onSuccess(adapted);
                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    onFailure(cause != null ? cause : ex);
                } catch (Throwable ex2) {
                    onFailure(ex2);
                }
            }

            @Override // org.springframework.util.concurrent.FailureCallback
            public void onFailure(Throwable ex) {
                failureCallback.onFailure(ex);
            }
        });
    }
}
