package io.netty.handler.codec.http;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpRequest.class */
public interface HttpRequest extends HttpMessage {
    @Deprecated
    HttpMethod getMethod();

    HttpMethod method();

    HttpRequest setMethod(HttpMethod httpMethod);

    @Deprecated
    String getUri();

    String uri();

    HttpRequest setUri(String str);

    HttpRequest setProtocolVersion(HttpVersion httpVersion);
}
