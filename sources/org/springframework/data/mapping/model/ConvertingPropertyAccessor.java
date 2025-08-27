package org.springframework.data.mapping.model;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/ConvertingPropertyAccessor.class */
public class ConvertingPropertyAccessor implements PersistentPropertyAccessor {
    private final PersistentPropertyAccessor accessor;
    private final ConversionService conversionService;

    public ConvertingPropertyAccessor(PersistentPropertyAccessor accessor, ConversionService conversionService) {
        Assert.notNull(accessor, "PersistentPropertyAccessor must not be null!");
        Assert.notNull(conversionService, "ConversionService must not be null!");
        this.accessor = accessor;
        this.conversionService = conversionService;
    }

    @Override // org.springframework.data.mapping.PersistentPropertyAccessor
    public void setProperty(PersistentProperty<?> property, Object value) {
        this.accessor.setProperty(property, convertIfNecessary(value, property.getType()));
    }

    @Override // org.springframework.data.mapping.PersistentPropertyAccessor
    public Object getProperty(PersistentProperty<?> property) {
        return this.accessor.getProperty(property);
    }

    public <T> T getProperty(PersistentProperty<?> persistentProperty, Class<T> cls) {
        Assert.notNull(persistentProperty, "PersistentProperty must not be null!");
        Assert.notNull(cls, "Target type must not be null!");
        return (T) convertIfNecessary(getProperty(persistentProperty), cls);
    }

    @Override // org.springframework.data.mapping.PersistentPropertyAccessor
    public Object getBean() {
        return this.accessor.getBean();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T convertIfNecessary(Object obj, Class<T> cls) {
        if (obj != 0 && !cls.isAssignableFrom(obj.getClass())) {
            return (T) this.conversionService.convert(obj, cls);
        }
        return obj;
    }
}
