package org.springframework.plugin.core.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/config/PluginListDefinitionParser.class */
public class PluginListDefinitionParser extends AbstractBeanDefinitionParser {
    protected static final String PACKAGE = "org.springframework.plugin.core.support.";

    protected String getPostProcessorName() {
        return "org.springframework.plugin.core.support.BeanListFactoryBean";
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext context) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(getPostProcessorName());
        builder.addPropertyValue("type", element.getAttribute("class"));
        return getSourcedBeanDefinition(builder, element, context);
    }

    private AbstractBeanDefinition getSourcedBeanDefinition(BeanDefinitionBuilder builder, Object source, ParserContext context) {
        AbstractBeanDefinition definition = builder.getRawBeanDefinition();
        definition.setSource(context.extractSource(source));
        return definition;
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }
}
