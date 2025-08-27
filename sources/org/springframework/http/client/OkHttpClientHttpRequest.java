package org.springframework.http.client;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/OkHttpClientHttpRequest.class */
class OkHttpClientHttpRequest extends AbstractBufferingClientHttpRequest {
    private final OkHttpClient client;
    private final URI uri;
    private final HttpMethod method;

    public OkHttpClientHttpRequest(OkHttpClient client, URI uri, HttpMethod method) {
        this.client = client;
        this.uri = uri;
        this.method = method;
    }

    @Override // org.springframework.http.HttpRequest
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override // org.springframework.http.HttpRequest
    public URI getURI() {
        return this.uri;
    }

    @Override // org.springframework.http.client.AbstractBufferingClientHttpRequest
    protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] content) throws IOException {
        Request request = OkHttpClientHttpRequestFactory.buildRequest(headers, content, this.uri, this.method);
        return new OkHttpClientHttpResponse(this.client.newCall(request).execute());
    }
}
