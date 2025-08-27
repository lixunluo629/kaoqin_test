package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/ClientHttpRequest.class */
public interface ClientHttpRequest extends HttpRequest, HttpOutputMessage {
    ClientHttpResponse execute() throws IOException;
}
