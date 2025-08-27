package org.springframework.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/SimpleClientHttpResponse.class */
final class SimpleClientHttpResponse extends AbstractClientHttpResponse {
    private final HttpURLConnection connection;
    private HttpHeaders headers;
    private InputStream responseStream;

    SimpleClientHttpResponse(HttpURLConnection connection) {
        this.connection = connection;
    }

    @Override // org.springframework.http.client.ClientHttpResponse
    public int getRawStatusCode() throws IOException {
        return this.connection.getResponseCode();
    }

    @Override // org.springframework.http.client.ClientHttpResponse
    public String getStatusText() throws IOException {
        return this.connection.getResponseMessage();
    }

    @Override // org.springframework.http.HttpMessage
    public HttpHeaders getHeaders() {
        if (this.headers == null) {
            this.headers = new HttpHeaders();
            String name = this.connection.getHeaderFieldKey(0);
            if (StringUtils.hasLength(name)) {
                this.headers.add(name, this.connection.getHeaderField(0));
            }
            int i = 1;
            while (true) {
                String name2 = this.connection.getHeaderFieldKey(i);
                if (!StringUtils.hasLength(name2)) {
                    break;
                }
                this.headers.add(name2, this.connection.getHeaderField(i));
                i++;
            }
        }
        return this.headers;
    }

    @Override // org.springframework.http.HttpInputMessage
    public InputStream getBody() throws IOException {
        InputStream errorStream = this.connection.getErrorStream();
        this.responseStream = errorStream != null ? errorStream : this.connection.getInputStream();
        return this.responseStream;
    }

    @Override // org.springframework.http.client.ClientHttpResponse, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.responseStream != null) {
            try {
                StreamUtils.drain(this.responseStream);
                this.responseStream.close();
            } catch (Exception e) {
            }
        }
    }
}
