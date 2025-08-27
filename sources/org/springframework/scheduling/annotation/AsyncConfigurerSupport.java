package org.springframework.scheduling.annotation;

import java.util.concurrent.Executor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/annotation/AsyncConfigurerSupport.class */
public class AsyncConfigurerSupport implements AsyncConfigurer {
    @Override // org.springframework.scheduling.annotation.AsyncConfigurer
    public Executor getAsyncExecutor() {
        return null;
    }

    @Override // org.springframework.scheduling.annotation.AsyncConfigurer
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
