package org.springframework.data.mapping.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/BeanWrapper.class */
class BeanWrapper<T> implements PersistentPropertyAccessor {
    private final T bean;

    protected BeanWrapper(T bean) {
        Assert.notNull(bean, "Bean must not be null!");
        this.bean = bean;
    }

    @Override // org.springframework.data.mapping.PersistentPropertyAccessor
    public void setProperty(PersistentProperty<?> property, Object value) throws IllegalAccessException, IllegalArgumentException {
        Assert.notNull(property, "PersistentProperty must not be null!");
        try {
            if (!property.usePropertyAccess()) {
                ReflectionUtils.makeAccessible(property.getField());
                ReflectionUtils.setField(property.getField(), this.bean, value);
                return;
            }
            Method setter = property.getSetter();
            if (property.usePropertyAccess() && setter != null) {
                ReflectionUtils.makeAccessible(setter);
                ReflectionUtils.invokeMethod(setter, this.bean, value);
            }
        } catch (IllegalStateException e) {
            throw new MappingException("Could not set object property!", e);
        }
    }

    @Override // org.springframework.data.mapping.PersistentPropertyAccessor
    public Object getProperty(PersistentProperty<?> property) {
        return getProperty(property, property.getType());
    }

    public <S> S getProperty(PersistentProperty<?> persistentProperty, Class<? extends S> cls) {
        Assert.notNull(persistentProperty, "PersistentProperty must not be null!");
        try {
            if (!persistentProperty.usePropertyAccess()) {
                Field field = persistentProperty.getField();
                ReflectionUtils.makeAccessible(field);
                return (S) ReflectionUtils.getField(field, this.bean);
            }
            Method getter = persistentProperty.getGetter();
            if (persistentProperty.usePropertyAccess() && getter != null) {
                ReflectionUtils.makeAccessible(getter);
                return (S) ReflectionUtils.invokeMethod(getter, this.bean);
            }
            return null;
        } catch (IllegalStateException e) {
            throw new MappingException(String.format("Could not read property %s of %s!", persistentProperty.toString(), this.bean.toString()), e);
        }
    }

    @Override // org.springframework.data.mapping.PersistentPropertyAccessor
    public T getBean() {
        return this.bean;
    }
}
