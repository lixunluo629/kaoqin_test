package io.netty.handler.codec.http.websocketx.extensions;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/websocketx/extensions/WebSocketExtension.class */
public interface WebSocketExtension {
    public static final int RSV1 = 4;
    public static final int RSV2 = 2;
    public static final int RSV3 = 1;

    int rsv();

    WebSocketExtensionEncoder newExtensionEncoder();

    WebSocketExtensionDecoder newExtensionDecoder();
}
