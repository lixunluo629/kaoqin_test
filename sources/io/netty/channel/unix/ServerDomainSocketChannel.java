package io.netty.channel.unix;

import io.netty.channel.ServerChannel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/unix/ServerDomainSocketChannel.class */
public interface ServerDomainSocketChannel extends ServerChannel, UnixChannel {
    @Override // io.netty.channel.Channel
    DomainSocketAddress remoteAddress();

    @Override // io.netty.channel.Channel
    DomainSocketAddress localAddress();
}
