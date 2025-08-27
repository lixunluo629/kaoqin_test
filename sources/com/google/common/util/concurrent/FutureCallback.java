package com.google.common.util.concurrent;

import javax.annotation.Nullable;

/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/FutureCallback.class */
public interface FutureCallback<V> {
    void onSuccess(@Nullable V v);

    void onFailure(Throwable th);
}
