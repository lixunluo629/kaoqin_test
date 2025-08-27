package com.google.common.util.concurrent;

/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/AsyncFunction.class */
public interface AsyncFunction<I, O> {
    ListenableFuture<O> apply(I i) throws Exception;
}
