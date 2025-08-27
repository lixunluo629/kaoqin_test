package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.AttributeMap;
import java.net.SocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/Channel.class */
public interface Channel extends AttributeMap, ChannelOutboundInvoker, Comparable<Channel> {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/Channel$Unsafe.class */
    public interface Unsafe {
        RecvByteBufAllocator.Handle recvBufAllocHandle();

        SocketAddress localAddress();

        SocketAddress remoteAddress();

        void register(EventLoop eventLoop, ChannelPromise channelPromise);

        void bind(SocketAddress socketAddress, ChannelPromise channelPromise);

        void connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise);

        void disconnect(ChannelPromise channelPromise);

        void close(ChannelPromise channelPromise);

        void closeForcibly();

        void deregister(ChannelPromise channelPromise);

        void beginRead();

        void write(Object obj, ChannelPromise channelPromise);

        void flush();

        ChannelPromise voidPromise();

        ChannelOutboundBuffer outboundBuffer();
    }

    ChannelId id();

    EventLoop eventLoop();

    Channel parent();

    ChannelConfig config();

    boolean isOpen();

    boolean isRegistered();

    boolean isActive();

    ChannelMetadata metadata();

    SocketAddress localAddress();

    SocketAddress remoteAddress();

    ChannelFuture closeFuture();

    boolean isWritable();

    long bytesBeforeUnwritable();

    long bytesBeforeWritable();

    Unsafe unsafe();

    ChannelPipeline pipeline();

    ByteBufAllocator alloc();

    @Override // io.netty.channel.ChannelOutboundInvoker
    Channel read();

    @Override // io.netty.channel.ChannelOutboundInvoker
    Channel flush();
}
