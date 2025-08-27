package io.netty.bootstrap;

import io.netty.channel.Channel;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/bootstrap/ChannelFactory.class */
public interface ChannelFactory<T extends Channel> {
    T newChannel();
}
