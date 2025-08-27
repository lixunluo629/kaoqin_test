package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.Iterator;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/group/ChannelGroupFuture.class */
public interface ChannelGroupFuture extends Future<Void>, Iterable<ChannelFuture> {
    ChannelGroup group();

    ChannelFuture find(Channel channel);

    @Override // io.netty.util.concurrent.Future
    boolean isSuccess();

    @Override // io.netty.util.concurrent.Future
    ChannelGroupException cause();

    boolean isPartialSuccess();

    boolean isPartialFailure();

    @Override // io.netty.util.concurrent.Future
    Future<Void> addListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener);

    @Override // io.netty.util.concurrent.Future
    Future<Void> addListeners(GenericFutureListener<? extends Future<? super Void>>... genericFutureListenerArr);

    @Override // io.netty.util.concurrent.Future
    Future<Void> removeListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener);

    @Override // io.netty.util.concurrent.Future
    Future<Void> removeListeners(GenericFutureListener<? extends Future<? super Void>>... genericFutureListenerArr);

    @Override // io.netty.util.concurrent.Future
    Future<Void> await() throws InterruptedException;

    @Override // io.netty.util.concurrent.Future
    Future<Void> awaitUninterruptibly();

    @Override // io.netty.util.concurrent.Future
    Future<Void> syncUninterruptibly();

    @Override // io.netty.util.concurrent.Future
    Future<Void> sync() throws InterruptedException;

    @Override // java.lang.Iterable
    Iterator<ChannelFuture> iterator();
}
