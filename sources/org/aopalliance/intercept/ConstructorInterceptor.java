package org.aopalliance.intercept;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/aopalliance/intercept/ConstructorInterceptor.class */
public interface ConstructorInterceptor extends Interceptor {
    Object construct(ConstructorInvocation constructorInvocation) throws Throwable;
}
