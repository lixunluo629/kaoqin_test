package io.netty.channel.sctp;

import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/sctp/SctpChannelConfig.class */
public interface SctpChannelConfig extends ChannelConfig {
    boolean isSctpNoDelay();

    SctpChannelConfig setSctpNoDelay(boolean z);

    int getSendBufferSize();

    SctpChannelConfig setSendBufferSize(int i);

    int getReceiveBufferSize();

    SctpChannelConfig setReceiveBufferSize(int i);

    SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams();

    SctpChannelConfig setInitMaxStreams(SctpStandardSocketOptions.InitMaxStreams initMaxStreams);

    @Override // io.netty.channel.ChannelConfig
    SctpChannelConfig setConnectTimeoutMillis(int i);

    @Override // io.netty.channel.ChannelConfig
    @Deprecated
    SctpChannelConfig setMaxMessagesPerRead(int i);

    @Override // io.netty.channel.ChannelConfig
    SctpChannelConfig setWriteSpinCount(int i);

    @Override // io.netty.channel.ChannelConfig
    SctpChannelConfig setAllocator(ByteBufAllocator byteBufAllocator);

    @Override // io.netty.channel.ChannelConfig
    SctpChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator);

    @Override // io.netty.channel.ChannelConfig
    SctpChannelConfig setAutoRead(boolean z);

    @Override // io.netty.channel.ChannelConfig
    SctpChannelConfig setAutoClose(boolean z);

    @Override // io.netty.channel.ChannelConfig
    SctpChannelConfig setWriteBufferHighWaterMark(int i);

    @Override // io.netty.channel.ChannelConfig
    SctpChannelConfig setWriteBufferLowWaterMark(int i);

    @Override // io.netty.channel.ChannelConfig
    SctpChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark);

    @Override // io.netty.channel.ChannelConfig
    SctpChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator);
}
