package org.springframework.jndi;

import javax.naming.NamingException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jndi/TypeMismatchNamingException.class */
public class TypeMismatchNamingException extends NamingException {
    private Class<?> requiredType;
    private Class<?> actualType;

    public TypeMismatchNamingException(String jndiName, Class<?> requiredType, Class<?> actualType) {
        super("Object of type [" + actualType + "] available at JNDI location [" + jndiName + "] is not assignable to [" + requiredType.getName() + "]");
        this.requiredType = requiredType;
        this.actualType = actualType;
    }

    @Deprecated
    public TypeMismatchNamingException(String explanation) {
        super(explanation);
    }

    public final Class<?> getRequiredType() {
        return this.requiredType;
    }

    public final Class<?> getActualType() {
        return this.actualType;
    }
}
