package io.netty.channel;

import io.netty.channel.Channel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ChannelFactory.class */
public interface ChannelFactory<T extends Channel> extends io.netty.bootstrap.ChannelFactory<T> {
    @Override // io.netty.bootstrap.ChannelFactory
    T newChannel();
}
