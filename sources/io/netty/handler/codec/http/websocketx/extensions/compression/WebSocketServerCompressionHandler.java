package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandler;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/websocketx/extensions/compression/WebSocketServerCompressionHandler.class */
public class WebSocketServerCompressionHandler extends WebSocketServerExtensionHandler {
    public WebSocketServerCompressionHandler() {
        super(new PerMessageDeflateServerExtensionHandshaker(), new DeflateFrameServerExtensionHandshaker());
    }
}
