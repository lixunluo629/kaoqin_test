package org.springframework.beans.factory.config;

import java.beans.PropertyDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/InstantiationAwareBeanPostProcessor.class */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {
    Object postProcessBeforeInstantiation(Class<?> cls, String str) throws BeansException;

    boolean postProcessAfterInstantiation(Object obj, String str) throws BeansException;

    PropertyValues postProcessPropertyValues(PropertyValues propertyValues, PropertyDescriptor[] propertyDescriptorArr, Object obj, String str) throws BeansException;
}
