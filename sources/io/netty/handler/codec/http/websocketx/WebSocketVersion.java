package io.netty.handler.codec.http.websocketx;

import io.netty.util.AsciiString;
import org.apache.tomcat.websocket.Constants;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/websocketx/WebSocketVersion.class */
public enum WebSocketVersion {
    UNKNOWN(AsciiString.cached("")),
    V00(AsciiString.cached("0")),
    V07(AsciiString.cached("7")),
    V08(AsciiString.cached("8")),
    V13(AsciiString.cached(Constants.WS_VERSION_HEADER_VALUE));

    private final AsciiString headerValue;

    WebSocketVersion(AsciiString headerValue) {
        this.headerValue = headerValue;
    }

    public String toHttpHeaderValue() {
        return toAsciiString().toString();
    }

    AsciiString toAsciiString() {
        if (this == UNKNOWN) {
            throw new IllegalStateException("Unknown web socket version: " + this);
        }
        return this.headerValue;
    }
}
