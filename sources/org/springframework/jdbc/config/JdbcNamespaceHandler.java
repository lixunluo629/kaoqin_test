package org.springframework.jdbc.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/config/JdbcNamespaceHandler.class */
public class JdbcNamespaceHandler extends NamespaceHandlerSupport {
    @Override // org.springframework.beans.factory.xml.NamespaceHandler
    public void init() {
        registerBeanDefinitionParser("embedded-database", new EmbeddedDatabaseBeanDefinitionParser());
        registerBeanDefinitionParser("initialize-database", new InitializeDatabaseBeanDefinitionParser());
    }
}
