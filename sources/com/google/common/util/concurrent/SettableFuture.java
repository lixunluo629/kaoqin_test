package com.google.common.util.concurrent;

import javax.annotation.Nullable;

/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/SettableFuture.class */
public final class SettableFuture<V> extends AbstractFuture<V> {
    public static <V> SettableFuture<V> create() {
        return new SettableFuture<>();
    }

    private SettableFuture() {
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    public boolean set(@Nullable V value) {
        return super.set(value);
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    public boolean setException(Throwable throwable) {
        return super.setException(throwable);
    }
}
