package org.springframework.web.context.request.async;

import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/DeferredResultProcessingInterceptorAdapter.class */
public abstract class DeferredResultProcessingInterceptorAdapter implements DeferredResultProcessingInterceptor {
    @Override // org.springframework.web.context.request.async.DeferredResultProcessingInterceptor
    public <T> void beforeConcurrentHandling(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
    }

    @Override // org.springframework.web.context.request.async.DeferredResultProcessingInterceptor
    public <T> void preProcess(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
    }

    @Override // org.springframework.web.context.request.async.DeferredResultProcessingInterceptor
    public <T> void postProcess(NativeWebRequest request, DeferredResult<T> deferredResult, Object concurrentResult) throws Exception {
    }

    @Override // org.springframework.web.context.request.async.DeferredResultProcessingInterceptor
    public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        return true;
    }

    @Override // org.springframework.web.context.request.async.DeferredResultProcessingInterceptor
    public <T> void afterCompletion(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
    }
}
