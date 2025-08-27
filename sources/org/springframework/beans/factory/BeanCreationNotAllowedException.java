package org.springframework.beans.factory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/BeanCreationNotAllowedException.class */
public class BeanCreationNotAllowedException extends BeanCreationException {
    public BeanCreationNotAllowedException(String beanName, String msg) {
        super(beanName, msg);
    }
}
