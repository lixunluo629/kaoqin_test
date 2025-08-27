package io.netty.handler.codec.http2;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2FrameSizePolicy.class */
public interface Http2FrameSizePolicy {
    void maxFrameSize(int i) throws Http2Exception;

    int maxFrameSize();
}
