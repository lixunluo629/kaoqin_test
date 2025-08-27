package io.netty.util.concurrent;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/ProgressivePromise.class */
public interface ProgressivePromise<V> extends Promise<V>, ProgressiveFuture<V> {
    ProgressivePromise<V> setProgress(long j, long j2);

    boolean tryProgress(long j, long j2);

    ProgressivePromise<V> setSuccess(V v);

    @Override // io.netty.util.concurrent.Promise, io.netty.channel.ChannelPromise
    ProgressivePromise<V> setFailure(Throwable th);

    @Override // io.netty.util.concurrent.Promise, io.netty.util.concurrent.Future
    ProgressivePromise<V> addListener(GenericFutureListener<? extends Future<? super V>> genericFutureListener);

    @Override // io.netty.util.concurrent.Promise, io.netty.util.concurrent.Future
    ProgressivePromise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... genericFutureListenerArr);

    @Override // io.netty.util.concurrent.Promise, io.netty.util.concurrent.Future
    ProgressivePromise<V> removeListener(GenericFutureListener<? extends Future<? super V>> genericFutureListener);

    @Override // io.netty.util.concurrent.Promise, io.netty.util.concurrent.Future
    ProgressivePromise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... genericFutureListenerArr);

    @Override // io.netty.util.concurrent.Promise, io.netty.util.concurrent.Future
    ProgressivePromise<V> await() throws InterruptedException;

    @Override // io.netty.util.concurrent.Promise, io.netty.util.concurrent.Future
    ProgressivePromise<V> awaitUninterruptibly();

    @Override // io.netty.util.concurrent.Promise, io.netty.util.concurrent.Future
    ProgressivePromise<V> sync() throws InterruptedException;

    @Override // io.netty.util.concurrent.Promise, io.netty.util.concurrent.Future
    ProgressivePromise<V> syncUninterruptibly();
}
