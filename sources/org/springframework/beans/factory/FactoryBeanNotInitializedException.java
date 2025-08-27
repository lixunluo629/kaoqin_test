package org.springframework.beans.factory;

import org.springframework.beans.FatalBeanException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/FactoryBeanNotInitializedException.class */
public class FactoryBeanNotInitializedException extends FatalBeanException {
    public FactoryBeanNotInitializedException() {
        super("FactoryBean is not fully initialized yet");
    }

    public FactoryBeanNotInitializedException(String msg) {
        super(msg);
    }
}
