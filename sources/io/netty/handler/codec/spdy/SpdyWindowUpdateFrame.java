package io.netty.handler.codec.spdy;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyWindowUpdateFrame.class */
public interface SpdyWindowUpdateFrame extends SpdyFrame {
    int streamId();

    SpdyWindowUpdateFrame setStreamId(int i);

    int deltaWindowSize();

    SpdyWindowUpdateFrame setDeltaWindowSize(int i);
}
