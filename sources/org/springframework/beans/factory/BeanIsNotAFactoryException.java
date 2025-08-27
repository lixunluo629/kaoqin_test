package org.springframework.beans.factory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/BeanIsNotAFactoryException.class */
public class BeanIsNotAFactoryException extends BeanNotOfRequiredTypeException {
    public BeanIsNotAFactoryException(String name, Class<?> actualType) {
        super(name, FactoryBean.class, actualType);
    }
}
