package org.springframework.beans.factory.support;

import org.springframework.beans.FatalBeanException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/BeanDefinitionValidationException.class */
public class BeanDefinitionValidationException extends FatalBeanException {
    public BeanDefinitionValidationException(String msg) {
        super(msg);
    }

    public BeanDefinitionValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
