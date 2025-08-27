package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ChannelFuture.class */
public interface ChannelFuture extends Future<Void> {
    Channel channel();

    @Override // io.netty.util.concurrent.Future
    Future<Void> addListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener);

    @Override // io.netty.util.concurrent.Future
    Future<Void> addListeners(GenericFutureListener<? extends Future<? super Void>>... genericFutureListenerArr);

    @Override // io.netty.util.concurrent.Future
    Future<Void> removeListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener);

    @Override // io.netty.util.concurrent.Future
    Future<Void> removeListeners(GenericFutureListener<? extends Future<? super Void>>... genericFutureListenerArr);

    @Override // io.netty.util.concurrent.Future
    Future<Void> sync() throws InterruptedException;

    @Override // io.netty.util.concurrent.Future
    Future<Void> syncUninterruptibly();

    @Override // io.netty.util.concurrent.Future
    Future<Void> await() throws InterruptedException;

    @Override // io.netty.util.concurrent.Future
    Future<Void> awaitUninterruptibly();

    boolean isVoid();
}
