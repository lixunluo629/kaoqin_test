package org.springframework.aop.interceptor;

import java.lang.reflect.Method;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/interceptor/AsyncUncaughtExceptionHandler.class */
public interface AsyncUncaughtExceptionHandler {
    void handleUncaughtException(Throwable th, Method method, Object... objArr);
}
