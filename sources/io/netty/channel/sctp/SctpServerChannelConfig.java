package io.netty.channel.sctp;

import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/sctp/SctpServerChannelConfig.class */
public interface SctpServerChannelConfig extends ChannelConfig {
    int getBacklog();

    SctpServerChannelConfig setBacklog(int i);

    int getSendBufferSize();

    SctpServerChannelConfig setSendBufferSize(int i);

    int getReceiveBufferSize();

    SctpServerChannelConfig setReceiveBufferSize(int i);

    SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams();

    SctpServerChannelConfig setInitMaxStreams(SctpStandardSocketOptions.InitMaxStreams initMaxStreams);

    @Override // io.netty.channel.ChannelConfig
    @Deprecated
    SctpServerChannelConfig setMaxMessagesPerRead(int i);

    @Override // io.netty.channel.ChannelConfig
    SctpServerChannelConfig setWriteSpinCount(int i);

    @Override // io.netty.channel.ChannelConfig
    SctpServerChannelConfig setConnectTimeoutMillis(int i);

    @Override // io.netty.channel.ChannelConfig
    SctpServerChannelConfig setAllocator(ByteBufAllocator byteBufAllocator);

    @Override // io.netty.channel.ChannelConfig
    SctpServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator);

    @Override // io.netty.channel.ChannelConfig
    SctpServerChannelConfig setAutoRead(boolean z);

    @Override // io.netty.channel.ChannelConfig
    SctpServerChannelConfig setAutoClose(boolean z);

    @Override // io.netty.channel.ChannelConfig
    SctpServerChannelConfig setWriteBufferHighWaterMark(int i);

    @Override // io.netty.channel.ChannelConfig
    SctpServerChannelConfig setWriteBufferLowWaterMark(int i);

    @Override // io.netty.channel.ChannelConfig
    SctpServerChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark);

    @Override // io.netty.channel.ChannelConfig
    SctpServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator);
}
