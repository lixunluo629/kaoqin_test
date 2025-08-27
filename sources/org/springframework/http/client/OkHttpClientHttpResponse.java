package org.springframework.http.client;

import com.squareup.okhttp.Response;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/OkHttpClientHttpResponse.class */
class OkHttpClientHttpResponse extends AbstractClientHttpResponse {
    private final Response response;
    private HttpHeaders headers;

    public OkHttpClientHttpResponse(Response response) {
        Assert.notNull(response, "Response must not be null");
        this.response = response;
    }

    @Override // org.springframework.http.client.ClientHttpResponse
    public int getRawStatusCode() {
        return this.response.code();
    }

    @Override // org.springframework.http.client.ClientHttpResponse
    public String getStatusText() {
        return this.response.message();
    }

    @Override // org.springframework.http.HttpInputMessage
    public InputStream getBody() throws IOException {
        return this.response.body().byteStream();
    }

    @Override // org.springframework.http.HttpMessage
    public HttpHeaders getHeaders() {
        if (this.headers == null) {
            HttpHeaders headers = new HttpHeaders();
            for (String headerName : this.response.headers().names()) {
                for (String headerValue : this.response.headers(headerName)) {
                    headers.add(headerName, headerValue);
                }
            }
            this.headers = headers;
        }
        return this.headers;
    }

    @Override // org.springframework.http.client.ClientHttpResponse, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            this.response.body().close();
        } catch (IOException e) {
        }
    }
}
