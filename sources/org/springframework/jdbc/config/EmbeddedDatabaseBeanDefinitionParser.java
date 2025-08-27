package org.springframework.jdbc.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/config/EmbeddedDatabaseBeanDefinitionParser.class */
class EmbeddedDatabaseBeanDefinitionParser extends AbstractBeanDefinitionParser {
    static final String DB_NAME_ATTRIBUTE = "database-name";
    static final String GENERATE_NAME_ATTRIBUTE = "generate-name";

    EmbeddedDatabaseBeanDefinitionParser() {
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) EmbeddedDatabaseFactoryBean.class);
        setGenerateUniqueDatabaseNameFlag(element, builder);
        setDatabaseName(element, builder);
        setDatabaseType(element, builder);
        DatabasePopulatorConfigUtils.setDatabasePopulator(element, builder);
        builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
        return builder.getBeanDefinition();
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }

    private void setGenerateUniqueDatabaseNameFlag(Element element, BeanDefinitionBuilder builder) {
        String generateName = element.getAttribute(GENERATE_NAME_ATTRIBUTE);
        if (StringUtils.hasText(generateName)) {
            builder.addPropertyValue("generateUniqueDatabaseName", generateName);
        }
    }

    private void setDatabaseName(Element element, BeanDefinitionBuilder builder) {
        String name = element.getAttribute(DB_NAME_ATTRIBUTE);
        if (!StringUtils.hasText(name)) {
            name = element.getAttribute("id");
        }
        if (StringUtils.hasText(name)) {
            builder.addPropertyValue("databaseName", name);
        }
    }

    private void setDatabaseType(Element element, BeanDefinitionBuilder builder) {
        String type = element.getAttribute("type");
        if (StringUtils.hasText(type)) {
            builder.addPropertyValue("databaseType", type);
        }
    }
}
