package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/EventLoopGroup.class */
public interface EventLoopGroup extends EventExecutorGroup {
    EventLoop next();

    ChannelFuture register(Channel channel);

    ChannelFuture register(ChannelPromise channelPromise);

    @Deprecated
    ChannelFuture register(Channel channel, ChannelPromise channelPromise);
}
