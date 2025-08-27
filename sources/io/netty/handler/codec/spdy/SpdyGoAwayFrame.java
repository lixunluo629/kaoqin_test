package io.netty.handler.codec.spdy;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyGoAwayFrame.class */
public interface SpdyGoAwayFrame extends SpdyFrame {
    int lastGoodStreamId();

    SpdyGoAwayFrame setLastGoodStreamId(int i);

    SpdySessionStatus status();

    SpdyGoAwayFrame setStatus(SpdySessionStatus spdySessionStatus);
}
