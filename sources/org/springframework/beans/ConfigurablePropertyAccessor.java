package org.springframework.beans;

import org.springframework.core.convert.ConversionService;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/ConfigurablePropertyAccessor.class */
public interface ConfigurablePropertyAccessor extends PropertyAccessor, PropertyEditorRegistry, TypeConverter {
    void setConversionService(ConversionService conversionService);

    ConversionService getConversionService();

    void setExtractOldValueForEditor(boolean z);

    boolean isExtractOldValueForEditor();

    void setAutoGrowNestedPaths(boolean z);

    boolean isAutoGrowNestedPaths();
}
