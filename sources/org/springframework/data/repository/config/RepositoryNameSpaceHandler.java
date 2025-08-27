package org.springframework.data.repository.config;

import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryNameSpaceHandler.class */
public class RepositoryNameSpaceHandler extends NamespaceHandlerSupport {
    private static final BeanDefinitionParser PARSER = new ResourceReaderRepositoryPopulatorBeanDefinitionParser();

    @Override // org.springframework.beans.factory.xml.NamespaceHandler
    public void init() {
        registerBeanDefinitionParser("unmarshaller-populator", PARSER);
        registerBeanDefinitionParser("jackson-populator", PARSER);
        registerBeanDefinitionParser("jackson2-populator", PARSER);
    }
}
