package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/ClientHttpRequestExecution.class */
public interface ClientHttpRequestExecution {
    ClientHttpResponse execute(HttpRequest httpRequest, byte[] bArr) throws IOException;
}
