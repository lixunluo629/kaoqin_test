package org.springframework.web.context.request.async;

import java.util.concurrent.Callable;
import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/CallableProcessingInterceptorAdapter.class */
public abstract class CallableProcessingInterceptorAdapter implements CallableProcessingInterceptor {
    @Override // org.springframework.web.context.request.async.CallableProcessingInterceptor
    public <T> void beforeConcurrentHandling(NativeWebRequest request, Callable<T> task) throws Exception {
    }

    @Override // org.springframework.web.context.request.async.CallableProcessingInterceptor
    public <T> void preProcess(NativeWebRequest request, Callable<T> task) throws Exception {
    }

    @Override // org.springframework.web.context.request.async.CallableProcessingInterceptor
    public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {
    }

    @Override // org.springframework.web.context.request.async.CallableProcessingInterceptor
    public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
        return RESULT_NONE;
    }

    @Override // org.springframework.web.context.request.async.CallableProcessingInterceptor
    public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {
    }
}
