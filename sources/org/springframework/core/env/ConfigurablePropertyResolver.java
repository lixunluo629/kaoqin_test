package org.springframework.core.env;

import org.springframework.core.convert.support.ConfigurableConversionService;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/env/ConfigurablePropertyResolver.class */
public interface ConfigurablePropertyResolver extends PropertyResolver {
    ConfigurableConversionService getConversionService();

    void setConversionService(ConfigurableConversionService configurableConversionService);

    void setPlaceholderPrefix(String str);

    void setPlaceholderSuffix(String str);

    void setValueSeparator(String str);

    void setIgnoreUnresolvableNestedPlaceholders(boolean z);

    void setRequiredProperties(String... strArr);

    void validateRequiredProperties() throws MissingRequiredPropertiesException;
}
