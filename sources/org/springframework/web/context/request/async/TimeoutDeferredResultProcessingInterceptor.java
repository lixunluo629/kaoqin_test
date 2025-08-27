package org.springframework.web.context.request.async;

import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/TimeoutDeferredResultProcessingInterceptor.class */
public class TimeoutDeferredResultProcessingInterceptor extends DeferredResultProcessingInterceptorAdapter {
    @Override // org.springframework.web.context.request.async.DeferredResultProcessingInterceptorAdapter, org.springframework.web.context.request.async.DeferredResultProcessingInterceptor
    public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> result) throws Exception {
        result.setErrorResult(new AsyncRequestTimeoutException());
        return false;
    }
}
