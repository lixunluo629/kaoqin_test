package io.netty.handler.codec.http.websocketx.extensions;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/websocketx/extensions/WebSocketClientExtensionHandshaker.class */
public interface WebSocketClientExtensionHandshaker {
    WebSocketExtensionData newRequestData();

    WebSocketClientExtension handshakeExtension(WebSocketExtensionData webSocketExtensionData);
}
