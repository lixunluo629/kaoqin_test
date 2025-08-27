package org.springframework.web.context.request.async;

import java.util.concurrent.Callable;
import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/TimeoutCallableProcessingInterceptor.class */
public class TimeoutCallableProcessingInterceptor extends CallableProcessingInterceptorAdapter {
    @Override // org.springframework.web.context.request.async.CallableProcessingInterceptorAdapter, org.springframework.web.context.request.async.CallableProcessingInterceptor
    public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
        return new AsyncRequestTimeoutException();
    }
}
