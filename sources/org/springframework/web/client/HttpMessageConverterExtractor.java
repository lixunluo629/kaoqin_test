package org.springframework.web.client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/HttpMessageConverterExtractor.class */
public class HttpMessageConverterExtractor<T> implements ResponseExtractor<T> {
    private final Type responseType;
    private final Class<T> responseClass;
    private final List<HttpMessageConverter<?>> messageConverters;
    private final Log logger;

    public HttpMessageConverterExtractor(Class<T> responseType, List<HttpMessageConverter<?>> messageConverters) {
        this((Type) responseType, messageConverters);
    }

    public HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters) {
        this(responseType, messageConverters, LogFactory.getLog(HttpMessageConverterExtractor.class));
    }

    HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters, Log logger) {
        Assert.notNull(responseType, "'responseType' must not be null");
        Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
        this.responseType = responseType;
        this.responseClass = responseType instanceof Class ? (Class) responseType : null;
        this.messageConverters = messageConverters;
        this.logger = logger;
    }

    @Override // org.springframework.web.client.ResponseExtractor
    public T extractData(ClientHttpResponse clientHttpResponse) throws IOException {
        MessageBodyClientHttpResponseWrapper messageBodyClientHttpResponseWrapper = new MessageBodyClientHttpResponseWrapper(clientHttpResponse);
        if (!messageBodyClientHttpResponseWrapper.hasMessageBody() || messageBodyClientHttpResponseWrapper.hasEmptyMessageBody()) {
            return null;
        }
        MediaType contentType = getContentType(messageBodyClientHttpResponseWrapper);
        for (HttpMessageConverter<?> httpMessageConverter : this.messageConverters) {
            if (httpMessageConverter instanceof GenericHttpMessageConverter) {
                GenericHttpMessageConverter genericHttpMessageConverter = (GenericHttpMessageConverter) httpMessageConverter;
                if (genericHttpMessageConverter.canRead(this.responseType, null, contentType)) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Reading [" + this.responseType + "] as \"" + contentType + "\" using [" + httpMessageConverter + "]");
                    }
                    return (T) genericHttpMessageConverter.read(this.responseType, null, messageBodyClientHttpResponseWrapper);
                }
            }
            if (this.responseClass != null && httpMessageConverter.canRead(this.responseClass, contentType)) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Reading [" + this.responseClass.getName() + "] as \"" + contentType + "\" using [" + httpMessageConverter + "]");
                }
                return (T) httpMessageConverter.read2(this.responseClass, messageBodyClientHttpResponseWrapper);
            }
        }
        throw new RestClientException("Could not extract response: no suitable HttpMessageConverter found for response type [" + this.responseType + "] and content type [" + contentType + "]");
    }

    private MediaType getContentType(ClientHttpResponse response) {
        MediaType contentType = response.getHeaders().getContentType();
        if (contentType == null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No Content-Type header found, defaulting to application/octet-stream");
            }
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return contentType;
    }
}
