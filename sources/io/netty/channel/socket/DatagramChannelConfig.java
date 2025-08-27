package io.netty.channel.socket;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import java.net.InetAddress;
import java.net.NetworkInterface;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/socket/DatagramChannelConfig.class */
public interface DatagramChannelConfig extends ChannelConfig {
    int getSendBufferSize();

    DatagramChannelConfig setSendBufferSize(int i);

    int getReceiveBufferSize();

    DatagramChannelConfig setReceiveBufferSize(int i);

    int getTrafficClass();

    DatagramChannelConfig setTrafficClass(int i);

    boolean isReuseAddress();

    DatagramChannelConfig setReuseAddress(boolean z);

    boolean isBroadcast();

    DatagramChannelConfig setBroadcast(boolean z);

    boolean isLoopbackModeDisabled();

    DatagramChannelConfig setLoopbackModeDisabled(boolean z);

    int getTimeToLive();

    DatagramChannelConfig setTimeToLive(int i);

    InetAddress getInterface();

    DatagramChannelConfig setInterface(InetAddress inetAddress);

    NetworkInterface getNetworkInterface();

    DatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface);

    @Override // io.netty.channel.ChannelConfig
    @Deprecated
    DatagramChannelConfig setMaxMessagesPerRead(int i);

    @Override // io.netty.channel.ChannelConfig
    DatagramChannelConfig setWriteSpinCount(int i);

    @Override // io.netty.channel.ChannelConfig
    DatagramChannelConfig setConnectTimeoutMillis(int i);

    @Override // io.netty.channel.ChannelConfig
    DatagramChannelConfig setAllocator(ByteBufAllocator byteBufAllocator);

    @Override // io.netty.channel.ChannelConfig
    DatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator);

    @Override // io.netty.channel.ChannelConfig
    DatagramChannelConfig setAutoRead(boolean z);

    @Override // io.netty.channel.ChannelConfig
    DatagramChannelConfig setAutoClose(boolean z);

    @Override // io.netty.channel.ChannelConfig
    DatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator);

    @Override // io.netty.channel.ChannelConfig
    DatagramChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark);
}
