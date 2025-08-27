package org.springframework.web.context.request.async;

import java.util.concurrent.Callable;
import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/CallableProcessingInterceptor.class */
public interface CallableProcessingInterceptor {
    public static final Object RESULT_NONE = new Object();
    public static final Object RESPONSE_HANDLED = new Object();

    <T> void beforeConcurrentHandling(NativeWebRequest nativeWebRequest, Callable<T> callable) throws Exception;

    <T> void preProcess(NativeWebRequest nativeWebRequest, Callable<T> callable) throws Exception;

    <T> void postProcess(NativeWebRequest nativeWebRequest, Callable<T> callable, Object obj) throws Exception;

    <T> Object handleTimeout(NativeWebRequest nativeWebRequest, Callable<T> callable) throws Exception;

    <T> void afterCompletion(NativeWebRequest nativeWebRequest, Callable<T> callable) throws Exception;
}
