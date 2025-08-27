package org.springframework.beans.factory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/BeanCurrentlyInCreationException.class */
public class BeanCurrentlyInCreationException extends BeanCreationException {
    public BeanCurrentlyInCreationException(String beanName) {
        super(beanName, "Requested bean is currently in creation: Is there an unresolvable circular reference?");
    }

    public BeanCurrentlyInCreationException(String beanName, String msg) {
        super(beanName, msg);
    }
}
