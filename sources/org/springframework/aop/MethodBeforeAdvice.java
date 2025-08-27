package org.springframework.aop;

import java.lang.reflect.Method;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/MethodBeforeAdvice.class */
public interface MethodBeforeAdvice extends BeforeAdvice {
    void before(Method method, Object[] objArr, Object obj) throws Throwable;
}
