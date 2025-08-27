package io.netty.handler.codec.http.websocketx.extensions;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/websocketx/extensions/WebSocketServerExtensionHandshaker.class */
public interface WebSocketServerExtensionHandshaker {
    WebSocketServerExtension handshakeExtension(WebSocketExtensionData webSocketExtensionData);
}
