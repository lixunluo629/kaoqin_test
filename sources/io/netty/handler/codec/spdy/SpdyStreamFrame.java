package io.netty.handler.codec.spdy;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyStreamFrame.class */
public interface SpdyStreamFrame extends SpdyFrame {
    int streamId();

    SpdyStreamFrame setStreamId(int i);

    boolean isLast();

    SpdyStreamFrame setLast(boolean z);
}
