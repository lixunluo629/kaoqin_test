package org.springframework.beans;

import java.beans.PropertyDescriptor;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/BeanWrapper.class */
public interface BeanWrapper extends ConfigurablePropertyAccessor {
    void setAutoGrowCollectionLimit(int i);

    int getAutoGrowCollectionLimit();

    Object getWrappedInstance();

    Class<?> getWrappedClass();

    PropertyDescriptor[] getPropertyDescriptors();

    PropertyDescriptor getPropertyDescriptor(String str) throws InvalidPropertyException;
}
