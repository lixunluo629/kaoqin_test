package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.util.concurrent.ListenableFuture;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/AsyncClientHttpRequestInterceptor.class */
public interface AsyncClientHttpRequestInterceptor {
    ListenableFuture<ClientHttpResponse> intercept(HttpRequest httpRequest, byte[] bArr, AsyncClientHttpRequestExecution asyncClientHttpRequestExecution) throws IOException;
}
