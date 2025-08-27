package io.netty.handler.codec.http;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpResponse.class */
public interface HttpResponse extends HttpMessage {
    @Deprecated
    HttpResponseStatus getStatus();

    HttpResponseStatus status();

    HttpResponse setStatus(HttpResponseStatus httpResponseStatus);

    @Override // io.netty.handler.codec.http.HttpMessage, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
    HttpResponse setProtocolVersion(HttpVersion httpVersion);
}
