package io.netty.handler.codec.spdy;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyPingFrame.class */
public interface SpdyPingFrame extends SpdyFrame {
    int id();

    SpdyPingFrame setId(int i);
}
