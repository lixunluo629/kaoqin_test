package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2HeadersDecoder;
import java.io.Closeable;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2FrameReader.class */
public interface Http2FrameReader extends Closeable {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2FrameReader$Configuration.class */
    public interface Configuration {
        Http2HeadersDecoder.Configuration headersConfiguration();

        Http2FrameSizePolicy frameSizePolicy();
    }

    void readFrame(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, Http2FrameListener http2FrameListener) throws Http2Exception;

    Configuration configuration();

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();
}
