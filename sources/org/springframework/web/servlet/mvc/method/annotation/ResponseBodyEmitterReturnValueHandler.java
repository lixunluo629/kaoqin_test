package org.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitterReturnValueHandler.class */
public class ResponseBodyEmitterReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {
    private static final Log logger = LogFactory.getLog(ResponseBodyEmitterReturnValueHandler.class);
    private final List<HttpMessageConverter<?>> messageConverters;
    private final Map<Class<?>, ResponseBodyEmitterAdapter> adapterMap;

    public ResponseBodyEmitterReturnValueHandler(List<HttpMessageConverter<?>> messageConverters) {
        Assert.notEmpty(messageConverters, "HttpMessageConverter List must not be empty");
        this.messageConverters = messageConverters;
        this.adapterMap = new HashMap(4);
        this.adapterMap.put(ResponseBodyEmitter.class, new SimpleResponseBodyEmitterAdapter());
    }

    @Deprecated
    public Map<Class<?>, ResponseBodyEmitterAdapter> getAdapterMap() {
        return this.adapterMap;
    }

    private ResponseBodyEmitterAdapter getAdapterFor(Class<?> type) {
        if (type != null) {
            for (Class<?> adapteeType : getAdapterMap().keySet()) {
                if (adapteeType.isAssignableFrom(type)) {
                    return getAdapterMap().get(adapteeType);
                }
            }
            return null;
        }
        return null;
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public boolean supportsReturnType(MethodParameter returnType) {
        Class<?> bodyType = ResponseEntity.class.isAssignableFrom(returnType.getParameterType()) ? ResolvableType.forMethodParameter(returnType).getGeneric(0).resolve() : returnType.getParameterType();
        return getAdapterFor(bodyType) != null;
    }

    @Override // org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler
    public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType) {
        if (returnValue != null) {
            Object adaptFrom = returnValue;
            if (returnValue instanceof ResponseEntity) {
                adaptFrom = ((ResponseEntity) returnValue).getBody();
            }
            return (adaptFrom == null || getAdapterFor(adaptFrom.getClass()) == null) ? false : true;
        }
        return false;
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (returnValue == null) {
            mavContainer.setRequestHandled(true);
            return;
        }
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse(HttpServletResponse.class);
        ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
        if (returnValue instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity) returnValue;
            response.setStatus(responseEntity.getStatusCodeValue());
            outputMessage.getHeaders().putAll(responseEntity.getHeaders());
            returnValue = responseEntity.getBody();
            if (returnValue == null) {
                mavContainer.setRequestHandled(true);
                outputMessage.flush();
                return;
            }
        }
        ServletRequest request = (ServletRequest) webRequest.getNativeRequest(ServletRequest.class);
        ShallowEtagHeaderFilter.disableContentCaching(request);
        ResponseBodyEmitterAdapter adapter = getAdapterFor(returnValue.getClass());
        if (adapter == null) {
            throw new IllegalStateException("Could not find ResponseBodyEmitterAdapter for return value type: " + returnValue.getClass());
        }
        ResponseBodyEmitter emitter = adapter.adaptToEmitter(returnValue, outputMessage);
        emitter.extendResponse(outputMessage);
        outputMessage.getBody();
        outputMessage.flush();
        ServerHttpResponse outputMessage2 = new StreamingServletServerHttpResponse(outputMessage);
        DeferredResult<?> deferredResult = new DeferredResult<>(emitter.getTimeout());
        WebAsyncUtils.getAsyncManager(webRequest).startDeferredResultProcessing(deferredResult, mavContainer);
        HttpMessageConvertingHandler handler = new HttpMessageConvertingHandler(outputMessage2, deferredResult);
        emitter.initialize(handler);
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitterReturnValueHandler$SimpleResponseBodyEmitterAdapter.class */
    private static class SimpleResponseBodyEmitterAdapter implements ResponseBodyEmitterAdapter {
        private SimpleResponseBodyEmitterAdapter() {
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterAdapter
        public ResponseBodyEmitter adaptToEmitter(Object returnValue, ServerHttpResponse response) {
            Assert.isInstanceOf(ResponseBodyEmitter.class, returnValue, "ResponseBodyEmitter expected");
            return (ResponseBodyEmitter) returnValue;
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitterReturnValueHandler$HttpMessageConvertingHandler.class */
    private class HttpMessageConvertingHandler implements ResponseBodyEmitter.Handler {
        private final ServerHttpResponse outputMessage;
        private final DeferredResult<?> deferredResult;

        public HttpMessageConvertingHandler(ServerHttpResponse outputMessage, DeferredResult<?> deferredResult) {
            this.outputMessage = outputMessage;
            this.deferredResult = deferredResult;
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.Handler
        public void send(Object data, MediaType mediaType) throws IOException, HttpMessageNotWritableException {
            sendInternal(data, mediaType);
        }

        private <T> void sendInternal(T data, MediaType mediaType) throws IOException, HttpMessageNotWritableException {
            for (HttpMessageConverter<?> converter : ResponseBodyEmitterReturnValueHandler.this.messageConverters) {
                if (converter.canWrite(data.getClass(), mediaType)) {
                    converter.write(data, mediaType, this.outputMessage);
                    this.outputMessage.flush();
                    if (ResponseBodyEmitterReturnValueHandler.logger.isDebugEnabled()) {
                        ResponseBodyEmitterReturnValueHandler.logger.debug("Written [" + data + "] using [" + converter + "]");
                        return;
                    }
                    return;
                }
            }
            throw new IllegalArgumentException("No suitable converter for " + data.getClass());
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.Handler
        public void complete() {
            this.deferredResult.setResult(null);
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.Handler
        public void completeWithError(Throwable failure) {
            this.deferredResult.setErrorResult(failure);
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.Handler
        public void onTimeout(Runnable callback) {
            this.deferredResult.onTimeout(callback);
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.Handler
        public void onCompletion(Runnable callback) {
            this.deferredResult.onCompletion(callback);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitterReturnValueHandler$StreamingServletServerHttpResponse.class */
    private static class StreamingServletServerHttpResponse implements ServerHttpResponse {
        private final ServerHttpResponse delegate;
        private final HttpHeaders mutableHeaders = new HttpHeaders();

        public StreamingServletServerHttpResponse(ServerHttpResponse delegate) {
            this.delegate = delegate;
            this.mutableHeaders.putAll(delegate.getHeaders());
        }

        @Override // org.springframework.http.server.ServerHttpResponse
        public void setStatusCode(HttpStatus status) {
            this.delegate.setStatusCode(status);
        }

        @Override // org.springframework.http.HttpMessage
        public HttpHeaders getHeaders() {
            return this.mutableHeaders;
        }

        @Override // org.springframework.http.HttpOutputMessage
        public OutputStream getBody() throws IOException {
            return this.delegate.getBody();
        }

        @Override // org.springframework.http.server.ServerHttpResponse, java.io.Flushable
        public void flush() throws IOException {
            this.delegate.flush();
        }

        @Override // org.springframework.http.server.ServerHttpResponse, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.delegate.close();
        }
    }
}
