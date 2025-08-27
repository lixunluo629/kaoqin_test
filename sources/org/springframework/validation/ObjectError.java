package org.springframework.validation;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/ObjectError.class */
public class ObjectError extends DefaultMessageSourceResolvable {
    private final String objectName;

    public ObjectError(String objectName, String defaultMessage) {
        this(objectName, null, null, defaultMessage);
    }

    public ObjectError(String objectName, String[] codes, Object[] arguments, String defaultMessage) {
        super(codes, arguments, defaultMessage);
        Assert.notNull(objectName, "Object name must not be null");
        this.objectName = objectName;
    }

    public String getObjectName() {
        return this.objectName;
    }

    @Override // org.springframework.context.support.DefaultMessageSourceResolvable
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != getClass() || !super.equals(other)) {
            return false;
        }
        ObjectError otherError = (ObjectError) other;
        return getObjectName().equals(otherError.getObjectName());
    }

    @Override // org.springframework.context.support.DefaultMessageSourceResolvable
    public int hashCode() {
        return (super.hashCode() * 29) + getObjectName().hashCode();
    }

    @Override // org.springframework.context.support.DefaultMessageSourceResolvable
    public String toString() {
        return "Error in object '" + this.objectName + "': " + resolvableToString();
    }
}
