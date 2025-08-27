package io.netty.channel.socket;

import java.net.InetSocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/socket/SocketChannel.class */
public interface SocketChannel extends DuplexChannel {
    @Override // io.netty.channel.Channel
    ServerSocketChannel parent();

    @Override // io.netty.channel.Channel
    SocketChannelConfig config();

    @Override // io.netty.channel.Channel
    InetSocketAddress localAddress();

    @Override // io.netty.channel.Channel
    InetSocketAddress remoteAddress();
}
