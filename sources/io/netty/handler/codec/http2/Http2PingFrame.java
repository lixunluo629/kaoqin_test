package io.netty.handler.codec.http2;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2PingFrame.class */
public interface Http2PingFrame extends Http2Frame {
    boolean ack();

    long content();
}
