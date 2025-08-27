package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/AbstractClientHttpRequestFactoryWrapper.class */
public abstract class AbstractClientHttpRequestFactoryWrapper implements ClientHttpRequestFactory {
    private final ClientHttpRequestFactory requestFactory;

    protected abstract ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod, ClientHttpRequestFactory clientHttpRequestFactory) throws IOException;

    protected AbstractClientHttpRequestFactoryWrapper(ClientHttpRequestFactory requestFactory) {
        Assert.notNull(requestFactory, "ClientHttpRequestFactory must not be null");
        this.requestFactory = requestFactory;
    }

    @Override // org.springframework.http.client.ClientHttpRequestFactory
    public final ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        return createRequest(uri, httpMethod, this.requestFactory);
    }
}
