package org.aopalliance.intercept;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/aopalliance/intercept/MethodInterceptor.class */
public interface MethodInterceptor extends Interceptor {
    Object invoke(MethodInvocation methodInvocation) throws Throwable;
}
