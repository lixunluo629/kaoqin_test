package org.hibernate.validator.internal.engine.constraintvalidation;

import java.util.Collections;
import java.util.Map;
import org.hibernate.validator.internal.engine.path.PathImpl;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintvalidation/ConstraintViolationCreationContext.class */
public class ConstraintViolationCreationContext {
    private final String message;
    private final PathImpl propertyPath;
    private final Map<String, Object> expressionVariables;
    private final Object dynamicPayload;

    public ConstraintViolationCreationContext(String message, PathImpl property) {
        this(message, property, Collections.emptyMap(), null);
    }

    public ConstraintViolationCreationContext(String message, PathImpl property, Map<String, Object> expressionVariables, Object dynamicPayload) {
        this.message = message;
        this.propertyPath = property;
        this.expressionVariables = expressionVariables;
        this.dynamicPayload = dynamicPayload;
    }

    public final String getMessage() {
        return this.message;
    }

    public final PathImpl getPath() {
        return this.propertyPath;
    }

    public Map<String, Object> getExpressionVariables() {
        return this.expressionVariables;
    }

    public Object getDynamicPayload() {
        return this.dynamicPayload;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ConstraintViolationCreationContext{");
        sb.append("message='").append(this.message).append('\'');
        sb.append(", propertyPath=").append(this.propertyPath);
        sb.append(", messageParameters=").append(this.expressionVariables);
        sb.append(", dynamicPayload=").append(this.dynamicPayload);
        sb.append('}');
        return sb.toString();
    }
}
