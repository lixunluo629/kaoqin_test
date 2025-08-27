package org.springframework.http.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/OkHttp3ClientHttpRequestFactory.class */
public class OkHttp3ClientHttpRequestFactory implements ClientHttpRequestFactory, AsyncClientHttpRequestFactory, DisposableBean {
    private OkHttpClient client;
    private final boolean defaultClient;

    public OkHttp3ClientHttpRequestFactory() {
        this.client = new OkHttpClient();
        this.defaultClient = true;
    }

    public OkHttp3ClientHttpRequestFactory(OkHttpClient client) {
        Assert.notNull(client, "OkHttpClient must not be null");
        this.client = client;
        this.defaultClient = false;
    }

    public void setReadTimeout(int readTimeout) {
        this.client = this.client.newBuilder().readTimeout(readTimeout, TimeUnit.MILLISECONDS).build();
    }

    public void setWriteTimeout(int writeTimeout) {
        this.client = this.client.newBuilder().writeTimeout(writeTimeout, TimeUnit.MILLISECONDS).build();
    }

    public void setConnectTimeout(int connectTimeout) {
        this.client = this.client.newBuilder().connectTimeout(connectTimeout, TimeUnit.MILLISECONDS).build();
    }

    @Override // org.springframework.http.client.ClientHttpRequestFactory
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) {
        return new OkHttp3ClientHttpRequest(this.client, uri, httpMethod);
    }

    @Override // org.springframework.http.client.AsyncClientHttpRequestFactory
    public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) {
        return new OkHttp3AsyncClientHttpRequest(this.client, uri, httpMethod);
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws IOException {
        if (this.defaultClient) {
            Cache cache = this.client.cache();
            if (cache != null) {
                cache.close();
            }
            this.client.dispatcher().executorService().shutdown();
        }
    }

    static Request buildRequest(HttpHeaders headers, byte[] content, URI uri, HttpMethod method) throws MalformedURLException {
        MediaType contentType = getContentType(headers);
        RequestBody body = (content.length > 0 || okhttp3.internal.http.HttpMethod.requiresRequestBody(method.name())) ? RequestBody.create(contentType, content) : null;
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
