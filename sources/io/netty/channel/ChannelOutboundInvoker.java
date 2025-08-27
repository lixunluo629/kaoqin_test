package io.netty.channel;

import java.net.SocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ChannelOutboundInvoker.class */
public interface ChannelOutboundInvoker {
    ChannelFuture bind(SocketAddress socketAddress);

    ChannelFuture connect(SocketAddress socketAddress);

    ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2);

    ChannelFuture disconnect();

    ChannelFuture close();

    ChannelFuture deregister();

    ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise);

    ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise);

    ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise);

    ChannelFuture disconnect(ChannelPromise channelPromise);

    ChannelFuture close(ChannelPromise channelPromise);

    ChannelFuture deregister(ChannelPromise channelPromise);

    ChannelOutboundInvoker read();

    ChannelFuture write(Object obj);

    ChannelFuture write(Object obj, ChannelPromise channelPromise);

    ChannelOutboundInvoker flush();

    ChannelFuture writeAndFlush(Object obj, ChannelPromise channelPromise);

    ChannelFuture writeAndFlush(Object obj);

    ChannelPromise newPromise();

    ChannelProgressivePromise newProgressivePromise();

    ChannelFuture newSucceededFuture();

    ChannelFuture newFailedFuture(Throwable th);

    ChannelPromise voidPromise();
}
