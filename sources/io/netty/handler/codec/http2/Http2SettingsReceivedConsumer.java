package io.netty.handler.codec.http2;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2SettingsReceivedConsumer.class */
public interface Http2SettingsReceivedConsumer {
    void consumeReceivedSettings(Http2Settings http2Settings);
}
