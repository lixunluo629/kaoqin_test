package org.springframework.beans.factory;

import org.springframework.beans.FatalBeanException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/BeanInitializationException.class */
public class BeanInitializationException extends FatalBeanException {
    public BeanInitializationException(String msg) {
        super(msg);
    }

    public BeanInitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
