package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.lang.Exception;
import java.util.concurrent.TimeUnit;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CheckedFuture.class */
public interface CheckedFuture<V, X extends Exception> extends ListenableFuture<V> {
    V checkedGet() throws Exception;

    V checkedGet(long j, TimeUnit timeUnit) throws Exception;
}
