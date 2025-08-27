package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.util.concurrent.ListenableFuture;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/AsyncClientHttpRequest.class */
public interface AsyncClientHttpRequest extends HttpRequest, HttpOutputMessage {
    ListenableFuture<ClientHttpResponse> executeAsync() throws IOException;
}
