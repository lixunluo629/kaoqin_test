package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/AsyncSettableFuture.class */
final class AsyncSettableFuture<V> extends ForwardingListenableFuture<V> {
    private final NestedFuture<V> nested = new NestedFuture<>();
    private final ListenableFuture<V> dereferenced = Futures.dereference(this.nested);

    public static <V> AsyncSettableFuture<V> create() {
        return new AsyncSettableFuture<>();
    }

    private AsyncSettableFuture() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.util.concurrent.ForwardingListenableFuture, com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
    public ListenableFuture<V> delegate() {
        return this.dereferenced;
    }

    public boolean setFuture(ListenableFuture<? extends V> future) {
        return this.nested.setFuture((ListenableFuture) Preconditions.checkNotNull(future));
    }

    public boolean setValue(@Nullable V value) {
        return setFuture(Futures.immediateFuture(value));
    }

    public boolean setException(Throwable exception) {
        return setFuture(Futures.immediateFailedFuture(exception));
    }

    public boolean isSet() {
        return this.nested.isDone();
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/AsyncSettableFuture$NestedFuture.class */
    private static final class NestedFuture<V> extends AbstractFuture<ListenableFuture<? extends V>> {
        private NestedFuture() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        boolean setFuture(ListenableFuture<? extends V> listenableFuture) {
            boolean result = set(listenableFuture);
            if (isCancelled()) {
                listenableFuture.cancel(wasInterrupted());
            }
            return result;
        }
    }
}
