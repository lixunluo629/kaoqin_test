package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ProgressiveFuture;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ChannelProgressiveFuture.class */
public interface ChannelProgressiveFuture extends ChannelFuture, ProgressiveFuture<Void> {
    @Override // io.netty.channel.ChannelFuture, io.netty.util.concurrent.Future
    Future<Void> addListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener);

    @Override // io.netty.channel.ChannelFuture, io.netty.util.concurrent.Future
    Future<Void> addListeners(GenericFutureListener<? extends Future<? super Void>>... genericFutureListenerArr);

    @Override // io.netty.channel.ChannelFuture, io.netty.util.concurrent.Future
    Future<Void> removeListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener);

    @Override // io.netty.channel.ChannelFuture, io.netty.util.concurrent.Future
    Future<Void> removeListeners(GenericFutureListener<? extends Future<? super Void>>... genericFutureListenerArr);

    @Override // io.netty.channel.ChannelFuture, io.netty.util.concurrent.Future
    Future<Void> sync() throws InterruptedException;

    @Override // io.netty.channel.ChannelFuture, io.netty.util.concurrent.Future
    Future<Void> syncUninterruptibly();

    @Override // io.netty.channel.ChannelFuture, io.netty.util.concurrent.Future
    Future<Void> await() throws InterruptedException;

    @Override // io.netty.channel.ChannelFuture, io.netty.util.concurrent.Future
    Future<Void> awaitUninterruptibly();
}
