package org.springframework.jdbc.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.w3c.dom.Element;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/config/InitializeDatabaseBeanDefinitionParser.class */
class InitializeDatabaseBeanDefinitionParser extends AbstractBeanDefinitionParser {
    InitializeDatabaseBeanDefinitionParser() {
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition((Class<?>) DataSourceInitializer.class);
        builder.addPropertyReference("dataSource", element.getAttribute("data-source"));
        builder.addPropertyValue("enabled", element.getAttribute("enabled"));
        DatabasePopulatorConfigUtils.setDatabasePopulator(element, builder);
        builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
        return builder.getBeanDefinition();
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected boolean shouldGenerateId() {
        return true;
    }
}
