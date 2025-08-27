package org.springframework.beans;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/FatalBeanException.class */
public class FatalBeanException extends BeansException {
    public FatalBeanException(String msg) {
        super(msg);
    }

    public FatalBeanException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
