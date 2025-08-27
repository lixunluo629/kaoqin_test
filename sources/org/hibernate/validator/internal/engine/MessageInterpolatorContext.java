package org.hibernate.validator.internal.engine;

import java.util.Map;
import javax.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.messageinterpolation.HibernateMessageInterpolatorContext;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/MessageInterpolatorContext.class */
public class MessageInterpolatorContext implements HibernateMessageInterpolatorContext {
    private static final Log log = LoggerFactory.make();
    private final ConstraintDescriptor<?> constraintDescriptor;
    private final Object validatedValue;
    private final Class<?> rootBeanType;
    private final Map<String, Object> messageParameters;

    public MessageInterpolatorContext(ConstraintDescriptor<?> constraintDescriptor, Object validatedValue, Class<?> rootBeanType, Map<String, Object> messageParameters) {
        this.constraintDescriptor = constraintDescriptor;
        this.validatedValue = validatedValue;
        this.rootBeanType = rootBeanType;
        this.messageParameters = messageParameters;
    }

    @Override // javax.validation.MessageInterpolator.Context
    public ConstraintDescriptor<?> getConstraintDescriptor() {
        return this.constraintDescriptor;
    }

    @Override // javax.validation.MessageInterpolator.Context
    public Object getValidatedValue() {
        return this.validatedValue;
    }

    @Override // org.hibernate.validator.messageinterpolation.HibernateMessageInterpolatorContext
    public Class<?> getRootBeanType() {
        return this.rootBeanType;
    }

    public Map<String, Object> getMessageParameters() {
        return this.messageParameters;
    }

    @Override // javax.validation.MessageInterpolator.Context
    public <T> T unwrap(Class<T> type) {
        if (type.isAssignableFrom(HibernateMessageInterpolatorContext.class)) {
            return type.cast(this);
        }
        throw log.getTypeNotSupportedForUnwrappingException(type);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MessageInterpolatorContext that = (MessageInterpolatorContext) o;
        if (this.constraintDescriptor != null) {
            if (!this.constraintDescriptor.equals(that.constraintDescriptor)) {
                return false;
            }
        } else if (that.constraintDescriptor != null) {
            return false;
        }
        if (this.rootBeanType != null) {
            if (!this.rootBeanType.equals(that.rootBeanType)) {
                return false;
            }
        } else if (that.rootBeanType != null) {
            return false;
        }
        if (this.validatedValue != null) {
            if (!this.validatedValue.equals(that.validatedValue)) {
                return false;
            }
            return true;
        }
        if (that.validatedValue != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.constraintDescriptor != null ? this.constraintDescriptor.hashCode() : 0;
        return (31 * ((31 * result) + (this.validatedValue != null ? this.validatedValue.hashCode() : 0))) + (this.rootBeanType != null ? this.rootBeanType.hashCode() : 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MessageInterpolatorContext");
        sb.append("{constraintDescriptor=").append(this.constraintDescriptor);
        sb.append(", validatedValue=").append(this.validatedValue);
        sb.append('}');
        return sb.toString();
    }
}
