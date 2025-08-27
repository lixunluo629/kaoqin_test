package org.springframework.util.concurrent;

import java.util.concurrent.Future;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/concurrent/ListenableFuture.class */
public interface ListenableFuture<T> extends Future<T> {
    void addCallback(ListenableFutureCallback<? super T> listenableFutureCallback);

    void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback);
}
