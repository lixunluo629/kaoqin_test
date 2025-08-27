package org.springframework.aop.framework;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/AopProxy.class */
public interface AopProxy {
    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}
