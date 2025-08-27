package io.netty.channel.pool;

import io.netty.channel.Channel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/pool/AbstractChannelPoolHandler.class */
public abstract class AbstractChannelPoolHandler implements ChannelPoolHandler {
    @Override // io.netty.channel.pool.ChannelPoolHandler
    public void channelAcquired(Channel ch2) throws Exception {
    }

    @Override // io.netty.channel.pool.ChannelPoolHandler
    public void channelReleased(Channel ch2) throws Exception {
    }
}
