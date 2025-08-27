package io.netty.handler.codec.http2;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2WindowUpdateFrame.class */
public interface Http2WindowUpdateFrame extends Http2StreamFrame {
    int windowSizeIncrement();
}
