package io.netty.channel;

import java.net.SocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ChannelOutboundHandler.class */
public interface ChannelOutboundHandler extends ChannelHandler {
    void bind(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, ChannelPromise channelPromise) throws Exception;

    void connect(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) throws Exception;

    void disconnect(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception;

    void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception;

    void deregister(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception;

    void read(ChannelHandlerContext channelHandlerContext) throws Exception;

    void write(ChannelHandlerContext channelHandlerContext, Object obj, ChannelPromise channelPromise) throws Exception;

    void flush(ChannelHandlerContext channelHandlerContext) throws Exception;
}
