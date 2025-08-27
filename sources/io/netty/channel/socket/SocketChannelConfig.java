package io.netty.channel.socket;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/socket/SocketChannelConfig.class */
public interface SocketChannelConfig extends ChannelConfig {
    boolean isTcpNoDelay();

    SocketChannelConfig setTcpNoDelay(boolean z);

    int getSoLinger();

    SocketChannelConfig setSoLinger(int i);

    int getSendBufferSize();

    SocketChannelConfig setSendBufferSize(int i);

    int getReceiveBufferSize();

    SocketChannelConfig setReceiveBufferSize(int i);

    boolean isKeepAlive();

    SocketChannelConfig setKeepAlive(boolean z);

    int getTrafficClass();

    SocketChannelConfig setTrafficClass(int i);

    boolean isReuseAddress();

    SocketChannelConfig setReuseAddress(boolean z);

    SocketChannelConfig setPerformancePreferences(int i, int i2, int i3);

    boolean isAllowHalfClosure();

    SocketChannelConfig setAllowHalfClosure(boolean z);

    @Override // io.netty.channel.ChannelConfig
    SocketChannelConfig setConnectTimeoutMillis(int i);

    @Override // io.netty.channel.ChannelConfig
    @Deprecated
    SocketChannelConfig setMaxMessagesPerRead(int i);

    @Override // io.netty.channel.ChannelConfig
    SocketChannelConfig setWriteSpinCount(int i);

    @Override // io.netty.channel.ChannelConfig
    SocketChannelConfig setAllocator(ByteBufAllocator byteBufAllocator);

    @Override // io.netty.channel.ChannelConfig
    SocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator);

    @Override // io.netty.channel.ChannelConfig
    SocketChannelConfig setAutoRead(boolean z);

    @Override // io.netty.channel.ChannelConfig
    SocketChannelConfig setAutoClose(boolean z);

    @Override // io.netty.channel.ChannelConfig
    SocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator);

    @Override // io.netty.channel.ChannelConfig
    SocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark);
}
