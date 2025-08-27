package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpMethod;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/ClientHttpRequestFactory.class */
public interface ClientHttpRequestFactory {
    ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException;
}
