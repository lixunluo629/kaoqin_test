package org.springframework.beans;

import java.beans.PropertyChangeEvent;
import org.springframework.util.ClassUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/TypeMismatchException.class */
public class TypeMismatchException extends PropertyAccessException {
    public static final String ERROR_CODE = "typeMismatch";
    private transient Object value;
    private Class<?> requiredType;

    public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType) {
        this(propertyChangeEvent, requiredType, (Throwable) null);
    }

    public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType, Throwable cause) {
        super(propertyChangeEvent, "Failed to convert property value of type '" + ClassUtils.getDescriptiveType(propertyChangeEvent.getNewValue()) + "'" + (requiredType != null ? " to required type '" + ClassUtils.getQualifiedName(requiredType) + "'" : "") + (propertyChangeEvent.getPropertyName() != null ? " for property '" + propertyChangeEvent.getPropertyName() + "'" : ""), cause);
        this.value = propertyChangeEvent.getNewValue();
        this.requiredType = requiredType;
    }

    public TypeMismatchException(Object value, Class<?> requiredType) {
        this(value, requiredType, (Throwable) null);
    }

    public TypeMismatchException(Object value, Class<?> requiredType, Throwable cause) {
        super("Failed to convert value of type '" + ClassUtils.getDescriptiveType(value) + "'" + (requiredType != null ? " to required type '" + ClassUtils.getQualifiedName(requiredType) + "'" : ""), cause);
        this.value = value;
        this.requiredType = requiredType;
    }

    @Override // org.springframework.beans.PropertyAccessException
    public Object getValue() {
        return this.value;
    }

    public Class<?> getRequiredType() {
        return this.requiredType;
    }

    @Override // org.springframework.beans.PropertyAccessException, org.springframework.core.ErrorCoded
    public String getErrorCode() {
        return ERROR_CODE;
    }
}
