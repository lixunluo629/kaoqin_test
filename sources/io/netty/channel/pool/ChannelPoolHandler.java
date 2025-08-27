package io.netty.channel.pool;

import io.netty.channel.Channel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/pool/ChannelPoolHandler.class */
public interface ChannelPoolHandler {
    void channelReleased(Channel channel) throws Exception;

    void channelAcquired(Channel channel) throws Exception;

    void channelCreated(Channel channel) throws Exception;
}
