package org.springframework.http.server;

import java.net.InetSocketAddress;
import java.security.Principal;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/server/ServerHttpRequest.class */
public interface ServerHttpRequest extends HttpRequest, HttpInputMessage {
    Principal getPrincipal();

    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

    ServerHttpAsyncRequestControl getAsyncRequestControl(ServerHttpResponse serverHttpResponse);
}
