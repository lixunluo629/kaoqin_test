package org.springframework.data.repository.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.data.repository.core.support.PropertiesBasedNamedQueries;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/NamedQueriesBeanDefinitionParser.class */
public class NamedQueriesBeanDefinitionParser implements BeanDefinitionParser {
    private static final String ATTRIBUTE = "named-queries-location";
    private final String defaultLocation;

    public NamedQueriesBeanDefinitionParser(String defaultLocation) {
        Assert.hasText(defaultLocation, "DefaultLocation must not be null nor empty!");
        this.defaultLocation = defaultLocation;
    }

    @Override // org.springframework.beans.factory.xml.BeanDefinitionParser
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder properties = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) PropertiesFactoryBean.class);
        properties.addPropertyValue("locations", getDefaultedLocation(element));
        if (isDefaultLocation(element)) {
            properties.addPropertyValue("ignoreResourceNotFound", true);
        }
        AbstractBeanDefinition propertiesDefinition = properties.getBeanDefinition();
        propertiesDefinition.setSource(parserContext.extractSource(element));
        BeanDefinitionBuilder namedQueries = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) PropertiesBasedNamedQueries.class);
        namedQueries.addConstructorArgValue(propertiesDefinition);
        AbstractBeanDefinition namedQueriesDefinition = namedQueries.getBeanDefinition();
        namedQueriesDefinition.setSource(parserContext.extractSource(element));
        return namedQueriesDefinition;
    }

    private boolean isDefaultLocation(Element element) {
        return !StringUtils.hasText(element.getAttribute(ATTRIBUTE));
    }

    private String getDefaultedLocation(Element element) {
        String locations = element.getAttribute(ATTRIBUTE);
        return StringUtils.hasText(locations) ? locations : this.defaultLocation;
    }
}
