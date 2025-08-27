package org.springframework.boot.bind;

import org.springframework.beans.NotWritablePropertyException;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedBindingNotWritablePropertyException.class */
public class RelaxedBindingNotWritablePropertyException extends NotWritablePropertyException {
    private final String message;
    private final PropertyOrigin propertyOrigin;

    RelaxedBindingNotWritablePropertyException(NotWritablePropertyException ex, PropertyOrigin propertyOrigin) {
        super(ex.getBeanClass(), ex.getPropertyName());
        this.propertyOrigin = propertyOrigin;
        this.message = "Failed to bind '" + propertyOrigin.getName() + "' from '" + propertyOrigin.getSource().getName() + "' to '" + ex.getPropertyName() + "' property on '" + ex.getBeanClass().getName() + "'";
    }

    @Override // org.springframework.core.NestedRuntimeException, java.lang.Throwable
    public String getMessage() {
        return this.message;
    }

    public PropertyOrigin getPropertyOrigin() {
        return this.propertyOrigin;
    }
}
