package io.netty.util.concurrent;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/DefaultProgressivePromise.class */
public class DefaultProgressivePromise<V> extends DefaultPromise<V> implements ProgressivePromise<V> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.util.concurrent.ProgressivePromise
    public /* bridge */ /* synthetic */ Promise setSuccess(Object obj) {
        return setSuccess((DefaultProgressivePromise<V>) obj);
    }

    public DefaultProgressivePromise(EventExecutor executor) {
        super(executor);
    }

    protected DefaultProgressivePromise() {
    }

    public ProgressivePromise<V> setProgress(long progress, long total) {
        if (total < 0) {
            total = -1;
            if (progress < 0) {
                throw new IllegalArgumentException("progress: " + progress + " (expected: >= 0)");
            }
        } else if (progress < 0 || progress > total) {
            throw new IllegalArgumentException("progress: " + progress + " (expected: 0 <= progress <= total (" + total + "))");
        }
        if (isDone()) {
            throw new IllegalStateException("complete already");
        }
        notifyProgressiveListeners(progress, total);
        return this;
    }

    @Override // io.netty.util.concurrent.ProgressivePromise
    public boolean tryProgress(long progress, long total) {
        if (total < 0) {
            total = -1;
            if (progress < 0 || isDone()) {
                return false;
            }
        } else if (progress < 0 || progress > total || isDone()) {
            return false;
        }
        notifyProgressiveListeners(progress, total);
        return true;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: addListener */
    public ProgressivePromise<V> addListener2(GenericFutureListener<? extends Future<? super V>> listener) {
        super.addListener2((GenericFutureListener) listener);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: addListeners */
    public ProgressivePromise<V> addListeners2(GenericFutureListener<? extends Future<? super V>>... listeners) {
        super.addListeners2((GenericFutureListener[]) listeners);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: removeListener */
    public ProgressivePromise<V> removeListener2(GenericFutureListener<? extends Future<? super V>> listener) {
        super.removeListener2((GenericFutureListener) listener);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: removeListeners */
    public ProgressivePromise<V> removeListeners2(GenericFutureListener<? extends Future<? super V>>... listeners) {
        super.removeListeners2((GenericFutureListener[]) listeners);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: sync */
    public ProgressivePromise<V> sync2() throws InterruptedException {
        super.sync2();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: syncUninterruptibly */
    public ProgressivePromise<V> syncUninterruptibly2() {
        super.syncUninterruptibly2();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: await */
    public ProgressivePromise<V> await2() throws InterruptedException {
        super.await2();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: awaitUninterruptibly */
    public ProgressivePromise<V> awaitUninterruptibly2() {
        super.awaitUninterruptibly2();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.util.concurrent.ProgressivePromise
    public ProgressivePromise<V> setSuccess(V result) {
        super.setSuccess((DefaultProgressivePromise<V>) result);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.channel.ChannelPromise
    public ProgressivePromise<V> setFailure(Throwable cause) {
        super.setFailure(cause);
        return this;
    }
}
