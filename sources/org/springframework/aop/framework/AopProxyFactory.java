package org.springframework.aop.framework;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/AopProxyFactory.class */
public interface AopProxyFactory {
    AopProxy createAopProxy(AdvisedSupport advisedSupport) throws AopConfigException;
}
