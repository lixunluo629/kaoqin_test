package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.util.concurrent.ListenableFuture;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/AsyncClientHttpRequestExecution.class */
public interface AsyncClientHttpRequestExecution {
    ListenableFuture<ClientHttpResponse> executeAsync(HttpRequest httpRequest, byte[] bArr) throws IOException;
}
