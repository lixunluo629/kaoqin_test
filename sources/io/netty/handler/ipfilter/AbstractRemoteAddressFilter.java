package io.netty.handler.ipfilter;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ipfilter/AbstractRemoteAddressFilter.class */
public abstract class AbstractRemoteAddressFilter<T extends SocketAddress> extends ChannelInboundHandlerAdapter {
    protected abstract boolean accept(ChannelHandlerContext channelHandlerContext, T t) throws Exception;

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        handleNewChannel(ctx);
        ctx.fireChannelRegistered();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (!handleNewChannel(ctx)) {
            throw new IllegalStateException("cannot determine to accept or reject a channel: " + ctx.channel());
        }
        ctx.fireChannelActive();
    }

    private boolean handleNewChannel(ChannelHandlerContext ctx) throws Exception {
        SocketAddress socketAddressRemoteAddress = ctx.channel().remoteAddress();
        if (socketAddressRemoteAddress == null) {
            return false;
        }
        ctx.pipeline().remove(this);
        if (accept(ctx, socketAddressRemoteAddress)) {
            channelAccepted(ctx, socketAddressRemoteAddress);
            return true;
        }
        ChannelFuture rejectedFuture = channelRejected(ctx, socketAddressRemoteAddress);
        if (rejectedFuture != null) {
            rejectedFuture.addListener2((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE);
            return true;
        }
        ctx.close();
        return true;
    }

    protected void channelAccepted(ChannelHandlerContext ctx, T remoteAddress) {
    }

    protected ChannelFuture channelRejected(ChannelHandlerContext ctx, T remoteAddress) {
        return null;
    }
}
