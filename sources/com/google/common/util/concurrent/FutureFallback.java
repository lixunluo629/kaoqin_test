package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/FutureFallback.class */
public interface FutureFallback<V> {
    ListenableFuture<V> create(Throwable th) throws Exception;
}
