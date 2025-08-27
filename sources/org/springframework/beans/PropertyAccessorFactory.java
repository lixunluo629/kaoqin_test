package org.springframework.beans;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/PropertyAccessorFactory.class */
public abstract class PropertyAccessorFactory {
    public static BeanWrapper forBeanPropertyAccess(Object target) {
        return new BeanWrapperImpl(target);
    }

    public static ConfigurablePropertyAccessor forDirectFieldAccess(Object target) {
        return new DirectFieldAccessor(target);
    }
}
