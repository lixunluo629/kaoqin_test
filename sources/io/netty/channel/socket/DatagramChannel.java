package io.netty.channel.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/socket/DatagramChannel.class */
public interface DatagramChannel extends Channel {
    @Override // io.netty.channel.Channel
    DatagramChannelConfig config();

    @Override // io.netty.channel.Channel
    InetSocketAddress localAddress();

    @Override // io.netty.channel.Channel
    InetSocketAddress remoteAddress();

    boolean isConnected();

    ChannelFuture joinGroup(InetAddress inetAddress);

    ChannelFuture joinGroup(InetAddress inetAddress, ChannelPromise channelPromise);

    ChannelFuture joinGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface);

    ChannelFuture joinGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface, ChannelPromise channelPromise);

    ChannelFuture joinGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2);

    ChannelFuture joinGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise);

    ChannelFuture leaveGroup(InetAddress inetAddress);

    ChannelFuture leaveGroup(InetAddress inetAddress, ChannelPromise channelPromise);

    ChannelFuture leaveGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface);

    ChannelFuture leaveGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface, ChannelPromise channelPromise);

    ChannelFuture leaveGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2);

    ChannelFuture leaveGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise);

    ChannelFuture block(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2);

    ChannelFuture block(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise);

    ChannelFuture block(InetAddress inetAddress, InetAddress inetAddress2);

    ChannelFuture block(InetAddress inetAddress, InetAddress inetAddress2, ChannelPromise channelPromise);
}
