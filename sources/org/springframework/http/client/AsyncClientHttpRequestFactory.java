package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpMethod;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/AsyncClientHttpRequestFactory.class */
public interface AsyncClientHttpRequestFactory {
    AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException;
}
