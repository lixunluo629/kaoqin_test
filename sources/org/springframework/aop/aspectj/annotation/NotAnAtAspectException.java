package org.springframework.aop.aspectj.annotation;

import org.springframework.aop.framework.AopConfigException;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/annotation/NotAnAtAspectException.class */
public class NotAnAtAspectException extends AopConfigException {
    private Class<?> nonAspectClass;

    public NotAnAtAspectException(Class<?> nonAspectClass) {
        super(nonAspectClass.getName() + " is not an @AspectJ aspect");
        this.nonAspectClass = nonAspectClass;
    }

    public Class<?> getNonAspectClass() {
        return this.nonAspectClass;
    }
}
