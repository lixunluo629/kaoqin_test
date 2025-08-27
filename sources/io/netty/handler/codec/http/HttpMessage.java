package io.netty.handler.codec.http;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpMessage.class */
public interface HttpMessage extends HttpObject {
    @Deprecated
    HttpVersion getProtocolVersion();

    HttpVersion protocolVersion();

    HttpMessage setProtocolVersion(HttpVersion httpVersion);

    HttpHeaders headers();
}
