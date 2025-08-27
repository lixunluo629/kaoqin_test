package org.springframework.beans;

import java.beans.PropertyChangeEvent;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/ConversionNotSupportedException.class */
public class ConversionNotSupportedException extends TypeMismatchException {
    public ConversionNotSupportedException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType, Throwable cause) {
        super(propertyChangeEvent, requiredType, cause);
    }

    public ConversionNotSupportedException(Object value, Class<?> requiredType, Throwable cause) {
        super(value, requiredType, cause);
    }
}
