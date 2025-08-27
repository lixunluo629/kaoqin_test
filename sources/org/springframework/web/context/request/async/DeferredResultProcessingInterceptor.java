package org.springframework.web.context.request.async;

import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/DeferredResultProcessingInterceptor.class */
public interface DeferredResultProcessingInterceptor {
    <T> void beforeConcurrentHandling(NativeWebRequest nativeWebRequest, DeferredResult<T> deferredResult) throws Exception;

    <T> void preProcess(NativeWebRequest nativeWebRequest, DeferredResult<T> deferredResult) throws Exception;

    <T> void postProcess(NativeWebRequest nativeWebRequest, DeferredResult<T> deferredResult, Object obj) throws Exception;

    <T> boolean handleTimeout(NativeWebRequest nativeWebRequest, DeferredResult<T> deferredResult) throws Exception;

    <T> void afterCompletion(NativeWebRequest nativeWebRequest, DeferredResult<T> deferredResult) throws Exception;
}
