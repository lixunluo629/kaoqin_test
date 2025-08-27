package io.netty.handler.codec.http.websocketx;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/websocketx/WebSocketHandshakeException.class */
public class WebSocketHandshakeException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public WebSocketHandshakeException(String s) {
        super(s);
    }

    public WebSocketHandshakeException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
