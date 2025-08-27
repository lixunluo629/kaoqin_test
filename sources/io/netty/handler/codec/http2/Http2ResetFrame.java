package io.netty.handler.codec.http2;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2ResetFrame.class */
public interface Http2ResetFrame extends Http2StreamFrame {
    long errorCode();
}
