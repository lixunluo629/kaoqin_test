package io.netty.channel.udt;

import io.netty.channel.Channel;
import java.net.InetSocketAddress;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/udt/UdtChannel.class */
public interface UdtChannel extends Channel {
    @Override // io.netty.channel.Channel
    UdtChannelConfig config();

    @Override // io.netty.channel.Channel
    InetSocketAddress localAddress();

    @Override // io.netty.channel.Channel
    InetSocketAddress remoteAddress();
}
