package io.netty.channel.socket;

import io.netty.channel.ServerChannel;
import java.net.InetSocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/socket/ServerSocketChannel.class */
public interface ServerSocketChannel extends ServerChannel {
    @Override // io.netty.channel.Channel
    ServerSocketChannelConfig config();

    @Override // io.netty.channel.Channel
    InetSocketAddress localAddress();

    @Override // io.netty.channel.Channel
    InetSocketAddress remoteAddress();
}
