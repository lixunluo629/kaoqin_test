package org.springframework.aop;

import java.lang.reflect.Method;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/AfterReturningAdvice.class */
public interface AfterReturningAdvice extends AfterAdvice {
    void afterReturning(Object obj, Method method, Object[] objArr, Object obj2) throws Throwable;
}
