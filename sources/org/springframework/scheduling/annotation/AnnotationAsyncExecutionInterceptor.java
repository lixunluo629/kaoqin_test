package org.springframework.scheduling.annotation;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import org.springframework.aop.interceptor.AsyncExecutionInterceptor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.core.annotation.AnnotatedElementUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/annotation/AnnotationAsyncExecutionInterceptor.class */
public class AnnotationAsyncExecutionInterceptor extends AsyncExecutionInterceptor {
    public AnnotationAsyncExecutionInterceptor(Executor defaultExecutor) {
        super(defaultExecutor);
    }

    public AnnotationAsyncExecutionInterceptor(Executor defaultExecutor, AsyncUncaughtExceptionHandler exceptionHandler) {
        super(defaultExecutor, exceptionHandler);
    }

    @Override // org.springframework.aop.interceptor.AsyncExecutionInterceptor, org.springframework.aop.interceptor.AsyncExecutionAspectSupport
    protected String getExecutorQualifier(Method method) {
        Async async = (Async) AnnotatedElementUtils.findMergedAnnotation(method, Async.class);
        if (async == null) {
            async = (Async) AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), Async.class);
        }
        if (async != null) {
            return async.value();
        }
        return null;
    }
}
