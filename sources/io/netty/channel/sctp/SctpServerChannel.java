package io.netty.channel.sctp;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ServerChannel;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Set;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/sctp/SctpServerChannel.class */
public interface SctpServerChannel extends ServerChannel {
    @Override // io.netty.channel.Channel
    SctpServerChannelConfig config();

    @Override // io.netty.channel.Channel
    InetSocketAddress localAddress();

    Set<InetSocketAddress> allLocalAddresses();

    ChannelFuture bindAddress(InetAddress inetAddress);

    ChannelFuture bindAddress(InetAddress inetAddress, ChannelPromise channelPromise);

    ChannelFuture unbindAddress(InetAddress inetAddress);

    ChannelFuture unbindAddress(InetAddress inetAddress, ChannelPromise channelPromise);
}
