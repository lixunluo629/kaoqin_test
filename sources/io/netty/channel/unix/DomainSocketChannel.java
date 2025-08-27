package io.netty.channel.unix;

import io.netty.channel.socket.DuplexChannel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/unix/DomainSocketChannel.class */
public interface DomainSocketChannel extends UnixChannel, DuplexChannel {
    @Override // io.netty.channel.Channel
    DomainSocketAddress remoteAddress();

    @Override // io.netty.channel.Channel
    DomainSocketAddress localAddress();

    @Override // io.netty.channel.Channel
    DomainSocketChannelConfig config();
}
