package org.springframework.data.util;

import java.lang.reflect.Field;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NotWritablePropertyException;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/DirectFieldAccessFallbackBeanWrapper.class */
public class DirectFieldAccessFallbackBeanWrapper extends BeanWrapperImpl {
    public DirectFieldAccessFallbackBeanWrapper(Object entity) {
        super(entity);
    }

    public DirectFieldAccessFallbackBeanWrapper(Class<?> type) {
        super(type);
    }

    @Override // org.springframework.beans.AbstractNestablePropertyAccessor, org.springframework.beans.AbstractPropertyAccessor, org.springframework.beans.PropertyAccessor
    public Object getPropertyValue(String propertyName) {
        try {
            return super.getPropertyValue(propertyName);
        } catch (NotReadablePropertyException e) {
            Field field = org.springframework.util.ReflectionUtils.findField(getWrappedClass(), propertyName);
            if (field == null) {
                throw new NotReadablePropertyException(getWrappedClass(), propertyName, "Could not find field for property during fallback access!");
            }
            org.springframework.util.ReflectionUtils.makeAccessible(field);
            return org.springframework.util.ReflectionUtils.getField(field, getWrappedInstance());
        }
    }

    @Override // org.springframework.beans.AbstractNestablePropertyAccessor, org.springframework.beans.AbstractPropertyAccessor, org.springframework.beans.PropertyAccessor
    public void setPropertyValue(String propertyName, Object value) throws IllegalAccessException, IllegalArgumentException {
        try {
            super.setPropertyValue(propertyName, value);
        } catch (NotWritablePropertyException e) {
            Field field = org.springframework.util.ReflectionUtils.findField(getWrappedClass(), propertyName);
            if (field == null) {
                throw new NotWritablePropertyException(getWrappedClass(), propertyName, "Could not find field for property during fallback access!", e);
            }
            org.springframework.util.ReflectionUtils.makeAccessible(field);
            org.springframework.util.ReflectionUtils.setField(field, getWrappedInstance(), value);
        }
    }
}
