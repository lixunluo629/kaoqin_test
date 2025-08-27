package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2FlowController.class */
public interface Http2FlowController {
    void channelHandlerContext(ChannelHandlerContext channelHandlerContext) throws Http2Exception;

    void initialWindowSize(int i) throws Http2Exception;

    int initialWindowSize();

    int windowSize(Http2Stream http2Stream);

    void incrementWindowSize(Http2Stream http2Stream, int i) throws Http2Exception;
}
