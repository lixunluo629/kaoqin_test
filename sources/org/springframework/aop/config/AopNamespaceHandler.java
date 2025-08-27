package org.springframework.aop.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/config/AopNamespaceHandler.class */
public class AopNamespaceHandler extends NamespaceHandlerSupport {
    @Override // org.springframework.beans.factory.xml.NamespaceHandler
    public void init() {
        registerBeanDefinitionParser("config", new ConfigBeanDefinitionParser());
        registerBeanDefinitionParser("aspectj-autoproxy", new AspectJAutoProxyBeanDefinitionParser());
        registerBeanDefinitionDecorator("scoped-proxy", new ScopedProxyBeanDefinitionDecorator());
        registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());
    }
}
