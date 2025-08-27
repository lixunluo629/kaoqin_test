package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.lang.Exception;
import java.util.concurrent.TimeUnit;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ForwardingCheckedFuture.class */
public abstract class ForwardingCheckedFuture<V, X extends Exception> extends ForwardingListenableFuture<V> implements CheckedFuture<V, X> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.util.concurrent.ForwardingListenableFuture, com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
    public abstract CheckedFuture<V, X> delegate();

    @Override // com.google.common.util.concurrent.CheckedFuture
    public V checkedGet() throws Exception {
        return delegate().checkedGet();
    }

    @Override // com.google.common.util.concurrent.CheckedFuture
    public V checkedGet(long timeout, TimeUnit unit) throws Exception {
        return delegate().checkedGet(timeout, unit);
    }

    @Beta
    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ForwardingCheckedFuture$SimpleForwardingCheckedFuture.class */
    public static abstract class SimpleForwardingCheckedFuture<V, X extends Exception> extends ForwardingCheckedFuture<V, X> {
        private final CheckedFuture<V, X> delegate;

        protected SimpleForwardingCheckedFuture(CheckedFuture<V, X> delegate) {
            this.delegate = (CheckedFuture) Preconditions.checkNotNull(delegate);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.util.concurrent.ForwardingCheckedFuture, com.google.common.util.concurrent.ForwardingListenableFuture, com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        public final CheckedFuture<V, X> delegate() {
            return this.delegate;
        }
    }
}
