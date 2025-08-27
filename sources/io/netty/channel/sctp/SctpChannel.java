package io.netty.channel.sctp;

import com.sun.nio.sctp.Association;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Set;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/sctp/SctpChannel.class */
public interface SctpChannel extends Channel {
    @Override // io.netty.channel.Channel
    SctpServerChannel parent();

    Association association();

    @Override // io.netty.channel.Channel
    InetSocketAddress localAddress();

    Set<InetSocketAddress> allLocalAddresses();

    @Override // io.netty.channel.Channel
    SctpChannelConfig config();

    @Override // io.netty.channel.Channel
    InetSocketAddress remoteAddress();

    Set<InetSocketAddress> allRemoteAddresses();

    ChannelFuture bindAddress(InetAddress inetAddress);

    ChannelFuture bindAddress(InetAddress inetAddress, ChannelPromise channelPromise);

    ChannelFuture unbindAddress(InetAddress inetAddress);

    ChannelFuture unbindAddress(InetAddress inetAddress, ChannelPromise channelPromise);
}
