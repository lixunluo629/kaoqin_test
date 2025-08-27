package io.netty.util.concurrent;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/SucceededFuture.class */
public final class SucceededFuture<V> extends CompleteFuture<V> {
    private final V result;

    public SucceededFuture(EventExecutor executor, V result) {
        super(executor);
        this.result = result;
    }

    @Override // io.netty.util.concurrent.Future
    public Throwable cause() {
        return null;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isSuccess() {
        return true;
    }

    @Override // io.netty.util.concurrent.Future
    public V getNow() {
        return this.result;
    }
}
