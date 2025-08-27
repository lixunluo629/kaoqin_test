package org.springframework.data.repository.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.data.repository.core.support.PropertiesBasedNamedQueries;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/NamedQueriesBeanDefinitionBuilder.class */
public class NamedQueriesBeanDefinitionBuilder {
    private final String defaultLocation;
    private String locations;

    public NamedQueriesBeanDefinitionBuilder(String defaultLocation) {
        Assert.hasText(defaultLocation, "DefaultLocation must not be null nor empty!");
        this.defaultLocation = defaultLocation;
    }

    public void setLocations(String locations) {
        Assert.hasText(locations, "Locations must not be null nor empty!");
        this.locations = locations;
    }

    public BeanDefinition build(Object source) {
        BeanDefinitionBuilder properties = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) PropertiesFactoryBean.class);
        String locationsToUse = StringUtils.hasText(this.locations) ? this.locations : this.defaultLocation;
        properties.addPropertyValue("locations", locationsToUse);
        if (!StringUtils.hasText(this.locations)) {
            properties.addPropertyValue("ignoreResourceNotFound", true);
        }
        AbstractBeanDefinition propertiesDefinition = properties.getBeanDefinition();
        propertiesDefinition.setSource(source);
        BeanDefinitionBuilder namedQueries = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) PropertiesBasedNamedQueries.class);
        namedQueries.addConstructorArgValue(propertiesDefinition);
        AbstractBeanDefinition namedQueriesDefinition = namedQueries.getBeanDefinition();
        namedQueriesDefinition.setSource(source);
        return namedQueriesDefinition;
    }
}
