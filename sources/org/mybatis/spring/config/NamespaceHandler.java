package org.mybatis.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/config/NamespaceHandler.class */
public class NamespaceHandler extends NamespaceHandlerSupport {
    @Override // org.springframework.beans.factory.xml.NamespaceHandler
    public void init() {
        registerBeanDefinitionParser("scan", new MapperScannerBeanDefinitionParser());
    }
}
