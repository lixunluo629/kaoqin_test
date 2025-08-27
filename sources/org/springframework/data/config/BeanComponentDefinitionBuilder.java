package org.springframework.data.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/config/BeanComponentDefinitionBuilder.class */
public class BeanComponentDefinitionBuilder {
    private final Element defaultSource;
    private final ParserContext context;

    public BeanComponentDefinitionBuilder(Element defaultSource, ParserContext context) {
        Assert.notNull(defaultSource, "DefaultSource must not be null!");
        Assert.notNull(context, "Context must not be null!");
        this.defaultSource = defaultSource;
        this.context = context;
    }

    public BeanComponentDefinition getComponent(BeanDefinitionBuilder builder) throws BeanDefinitionStoreException {
        Assert.notNull(builder, "Builder must not be null!");
        AbstractBeanDefinition definition = builder.getRawBeanDefinition();
        String name = BeanDefinitionReaderUtils.generateBeanName(definition, this.context.getRegistry(), this.context.isNested());
        return getComponent(builder, name);
    }

    public BeanComponentDefinition getComponentIdButFallback(BeanDefinitionBuilder builder, String fallback) {
        Assert.hasText(fallback, "Fallback component id must not be null or empty!");
        String id = this.defaultSource.getAttribute("id");
        return getComponent(builder, StringUtils.hasText(id) ? id : fallback);
    }

    public BeanComponentDefinition getComponent(BeanDefinitionBuilder builder, String name) {
        return getComponent(builder, name, this.defaultSource);
    }

    public BeanComponentDefinition getComponent(BeanDefinitionBuilder builder, String name, Object rawSource) {
        Assert.notNull(builder, "Builder must not be null!");
        Assert.hasText(name, "Name of bean must not be null or empty!");
        AbstractBeanDefinition definition = builder.getRawBeanDefinition();
        definition.setSource(this.context.extractSource(rawSource));
        return new BeanComponentDefinition(definition, name);
    }
}
