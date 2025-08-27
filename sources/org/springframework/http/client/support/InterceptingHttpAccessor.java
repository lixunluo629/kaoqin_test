package org.springframework.http.client.support;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/support/InterceptingHttpAccessor.class */
public abstract class InterceptingHttpAccessor extends HttpAccessor {
    private List<ClientHttpRequestInterceptor> interceptors = new ArrayList();

    public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public List<ClientHttpRequestInterceptor> getInterceptors() {
        return this.interceptors;
    }

    @Override // org.springframework.http.client.support.HttpAccessor
    public ClientHttpRequestFactory getRequestFactory() {
        ClientHttpRequestFactory delegate = super.getRequestFactory();
        if (!CollectionUtils.isEmpty(getInterceptors())) {
            return new InterceptingClientHttpRequestFactory(delegate, getInterceptors());
        }
        return delegate;
    }
}
