package org.springframework.http.client.support;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.AsyncClientHttpRequest;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.util.Assert;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/support/AsyncHttpAccessor.class */
public class AsyncHttpAccessor {
    protected final Log logger = LogFactory.getLog(getClass());
    private AsyncClientHttpRequestFactory asyncRequestFactory;

    public void setAsyncRequestFactory(AsyncClientHttpRequestFactory asyncRequestFactory) {
        Assert.notNull(asyncRequestFactory, "AsyncClientHttpRequestFactory must not be null");
        this.asyncRequestFactory = asyncRequestFactory;
    }

    public AsyncClientHttpRequestFactory getAsyncRequestFactory() {
        return this.asyncRequestFactory;
    }

    protected AsyncClientHttpRequest createAsyncRequest(URI url, HttpMethod method) throws IOException {
        AsyncClientHttpRequest request = getAsyncRequestFactory().createAsyncRequest(url, method);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Created asynchronous " + method.name() + " request for \"" + url + SymbolConstants.QUOTES_SYMBOL);
        }
        return request;
    }
}
