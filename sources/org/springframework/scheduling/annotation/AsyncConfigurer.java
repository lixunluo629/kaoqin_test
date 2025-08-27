package org.springframework.scheduling.annotation;

import java.util.concurrent.Executor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/annotation/AsyncConfigurer.class */
public interface AsyncConfigurer {
    Executor getAsyncExecutor();

    AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler();
}
