package org.springframework.data.config;

import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/config/ParsingUtils.class */
public abstract class ParsingUtils {
    private ParsingUtils() {
    }

    public static void setPropertyValue(BeanDefinitionBuilder builder, Element element, String attrName, String propertyName) {
        Assert.notNull(builder, "BeanDefinitionBuilder must not be null!");
        Assert.notNull(element, "Element must not be null!");
        Assert.hasText(attrName, "Attribute name must not be null!");
        Assert.hasText(propertyName, "Property name must not be null!");
        String attr = element.getAttribute(attrName);
        if (StringUtils.hasText(attr)) {
            builder.addPropertyValue(propertyName, attr);
        }
    }

    public static void setPropertyValue(BeanDefinitionBuilder builder, Element element, String attribute) {
        setPropertyValue(builder, element, attribute, attribute);
    }

    public static void setPropertyReference(BeanDefinitionBuilder builder, Element element, String attribute, String property) {
        Assert.notNull(builder, "BeanDefinitionBuilder must not be null!");
        Assert.notNull(element, "Element must not be null!");
        Assert.hasText(attribute, "Attribute name must not be null!");
        Assert.hasText(property, "Property name must not be null!");
        String value = element.getAttribute(attribute);
        if (StringUtils.hasText(value)) {
            builder.addPropertyReference(property, value);
        }
    }

    public static AbstractBeanDefinition getSourceBeanDefinition(BeanDefinitionBuilder builder, ParserContext context, Element element) {
        Assert.notNull(element, "Element must not be null!");
        Assert.notNull(context, "ParserContext must not be null!");
        return getSourceBeanDefinition(builder, context.extractSource(element));
    }

    public static AbstractBeanDefinition getSourceBeanDefinition(BeanDefinitionBuilder builder, Object source) {
        Assert.notNull(builder, "Builder must not be null!");
        AbstractBeanDefinition definition = builder.getRawBeanDefinition();
        definition.setSource(source);
        return definition;
    }

    public static AbstractBeanDefinition getObjectFactoryBeanDefinition(String targetBeanName, Object source) {
        Assert.hasText(targetBeanName, "Target bean name must not be null or empty!");
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) ObjectFactoryCreatingFactoryBean.class);
        builder.addPropertyValue("targetBeanName", targetBeanName);
        builder.setRole(2);
        return getSourceBeanDefinition(builder, source);
    }
}
