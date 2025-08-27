package org.springframework.boot.bind;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/PropertySourcesBinder.class */
public class PropertySourcesBinder {
    private PropertySources propertySources;
    private ConversionService conversionService;

    public PropertySourcesBinder(PropertySources propertySources) {
        this.propertySources = propertySources;
    }

    public PropertySourcesBinder(PropertySource<?> propertySource) {
        this(createPropertySources(propertySource));
    }

    public PropertySourcesBinder(ConfigurableEnvironment environment) {
        this(environment.getPropertySources());
    }

    public void setPropertySources(PropertySources propertySources) {
        this.propertySources = propertySources;
    }

    public PropertySources getPropertySources() {
        return this.propertySources;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public ConversionService getConversionService() {
        return this.conversionService;
    }

    public Map<String, Object> extractAll(String prefix) throws BeansException {
        Map<String, Object> content = new LinkedHashMap<>();
        bindTo(prefix, content);
        return content;
    }

    public void bindTo(String prefix, Object target) throws BeansException {
        PropertiesConfigurationFactory<Object> factory = new PropertiesConfigurationFactory<>(target);
        if (StringUtils.hasText(prefix)) {
            factory.setTargetName(prefix);
        }
        if (this.conversionService != null) {
            factory.setConversionService(this.conversionService);
        }
        factory.setPropertySources(this.propertySources);
        try {
            factory.bindPropertiesToTarget();
        } catch (BindException ex) {
            throw new IllegalStateException("Cannot bind to " + target, ex);
        }
    }

    private static PropertySources createPropertySources(PropertySource<?> propertySource) {
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addLast(propertySource);
        return propertySources;
    }
}
