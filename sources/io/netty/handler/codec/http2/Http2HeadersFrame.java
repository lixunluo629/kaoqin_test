package io.netty.handler.codec.http2;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2HeadersFrame.class */
public interface Http2HeadersFrame extends Http2StreamFrame {
    Http2Headers headers();

    int padding();

    boolean isEndStream();
}
