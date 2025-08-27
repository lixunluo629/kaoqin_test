package io.netty.channel;

import io.netty.util.concurrent.GenericFutureListener;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ChannelFutureListener.class */
public interface ChannelFutureListener extends GenericFutureListener<ChannelFuture> {
    public static final ChannelFutureListener CLOSE = new ChannelFutureListener() { // from class: io.netty.channel.ChannelFutureListener.1
        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture future) {
            future.channel().close();
        }
    };
    public static final ChannelFutureListener CLOSE_ON_FAILURE = new ChannelFutureListener() { // from class: io.netty.channel.ChannelFutureListener.2
        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture future) {
            if (!future.isSuccess()) {
                future.channel().close();
            }
        }
    };
    public static final ChannelFutureListener FIRE_EXCEPTION_ON_FAILURE = new ChannelFutureListener() { // from class: io.netty.channel.ChannelFutureListener.3
        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture future) {
            if (!future.isSuccess()) {
                future.channel().pipeline().fireExceptionCaught(future.cause());
            }
        }
    };
}
