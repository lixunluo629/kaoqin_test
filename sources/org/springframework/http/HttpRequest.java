package org.springframework.http;

import java.net.URI;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/HttpRequest.class */
public interface HttpRequest extends HttpMessage {
    HttpMethod getMethod();

    URI getURI();
}
