package org.springframework.beans;

import java.beans.PropertyChangeEvent;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/MethodInvocationException.class */
public class MethodInvocationException extends PropertyAccessException {
    public static final String ERROR_CODE = "methodInvocation";

    public MethodInvocationException(PropertyChangeEvent propertyChangeEvent, Throwable cause) {
        super(propertyChangeEvent, "Property '" + propertyChangeEvent.getPropertyName() + "' threw exception", cause);
    }

    @Override // org.springframework.beans.PropertyAccessException, org.springframework.core.ErrorCoded
    public String getErrorCode() {
        return ERROR_CODE;
    }
}
