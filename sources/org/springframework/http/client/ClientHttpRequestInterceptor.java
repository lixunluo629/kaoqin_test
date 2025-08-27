package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/ClientHttpRequestInterceptor.class */
public interface ClientHttpRequestInterceptor {
    ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bArr, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException;
}
