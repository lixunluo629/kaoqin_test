package org.springframework.beans.factory;

import org.springframework.beans.FatalBeanException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/CannotLoadBeanClassException.class */
public class CannotLoadBeanClassException extends FatalBeanException {
    private String resourceDescription;
    private String beanName;
    private String beanClassName;

    public CannotLoadBeanClassException(String resourceDescription, String beanName, String beanClassName, ClassNotFoundException cause) {
        super("Cannot find class [" + beanClassName + "] for bean with name '" + beanName + "'" + (resourceDescription != null ? " defined in " + resourceDescription : ""), cause);
        this.resourceDescription = resourceDescription;
        this.beanName = beanName;
        this.beanClassName = beanClassName;
    }

    public CannotLoadBeanClassException(String resourceDescription, String beanName, String beanClassName, LinkageError cause) {
        super("Error loading class [" + beanClassName + "] for bean with name '" + beanName + "'" + (resourceDescription != null ? " defined in " + resourceDescription : "") + ": problem with class file or dependent class", cause);
        this.resourceDescription = resourceDescription;
        this.beanName = beanName;
        this.beanClassName = beanClassName;
    }

    public String getResourceDescription() {
        return this.resourceDescription;
    }

    public String getBeanName() {
        return this.beanName;
    }

    public String getBeanClassName() {
        return this.beanClassName;
    }
}
