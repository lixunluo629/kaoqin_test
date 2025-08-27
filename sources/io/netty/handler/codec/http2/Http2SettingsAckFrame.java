package io.netty.handler.codec.http2;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2SettingsAckFrame.class */
public interface Http2SettingsAckFrame extends Http2Frame {
    public static final Http2SettingsAckFrame INSTANCE = new DefaultHttp2SettingsAckFrame();

    @Override // io.netty.handler.codec.http2.Http2Frame
    String name();
}
