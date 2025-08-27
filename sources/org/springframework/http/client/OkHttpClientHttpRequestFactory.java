package org.springframework.http.client;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/OkHttpClientHttpRequestFactory.class */
public class OkHttpClientHttpRequestFactory implements ClientHttpRequestFactory, AsyncClientHttpRequestFactory, DisposableBean {
    private final OkHttpClient client;
    private final boolean defaultClient;

    public OkHttpClientHttpRequestFactory() {
        this.client = new OkHttpClient();
        this.defaultClient = true;
    }

    public OkHttpClientHttpRequestFactory(OkHttpClient client) {
        Assert.notNull(client, "OkHttpClient must not be null");
        this.client = client;
        this.defaultClient = false;
    }

    public void setReadTimeout(int readTimeout) {
        this.client.setReadTimeout(readTimeout, TimeUnit.MILLISECONDS);
    }

    public void setWriteTimeout(int writeTimeout) {
        this.client.setWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS);
    }

    public void setConnectTimeout(int connectTimeout) {
        this.client.setConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
    }

    @Override // org.springframework.http.client.ClientHttpRequestFactory
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) {
        return new OkHttpClientHttpRequest(this.client, uri, httpMethod);
    }

    @Override // org.springframework.http.client.AsyncClientHttpRequestFactory
    public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) {
        return new OkHttpAsyncClientHttpRequest(this.client, uri, httpMethod);
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws IOException {
        if (this.defaultClient) {
            if (this.client.getCache() != null) {
                this.client.getCache().close();
            }
            this.client.getDispatcher().getExecutorService().shutdown();
        }
    }

    static Request buildRequest(HttpHeaders headers, byte[] content, URI uri, HttpMethod method) throws MalformedURLException {
        MediaType contentType = getContentType(headers);
        RequestBody body = (content.length > 0 || com.squareup.okhttp.internal.http.HttpMethod.requiresRequestBody(method.name())) ? RequestBody.create(contentType, content) : null;
        Request.Builder builder = new Request.Builder().url(uri.toURL()).method(method.name(), body);
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            for (String headerValue : entry.getValue()) {
                builder.addHeader(headerName, headerValue);
            }
        }
        return builder.build();
    }

    private static MediaType getContentType(HttpHeaders headers) {
        String rawContentType = headers.getFirst("Content-Type");
        if (StringUtils.hasText(rawContentType)) {
            return MediaType.parse(rawContentType);
        }
        return null;
    }
}
