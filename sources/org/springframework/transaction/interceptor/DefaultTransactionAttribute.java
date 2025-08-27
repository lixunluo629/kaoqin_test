package org.springframework.transaction.interceptor;

import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/interceptor/DefaultTransactionAttribute.class */
public class DefaultTransactionAttribute extends DefaultTransactionDefinition implements TransactionAttribute {
    private String qualifier;
    private String descriptor;

    public DefaultTransactionAttribute() {
    }

    public DefaultTransactionAttribute(TransactionAttribute other) {
        super(other);
    }

    public DefaultTransactionAttribute(int propagationBehavior) {
        super(propagationBehavior);
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override // org.springframework.transaction.interceptor.TransactionAttribute
    public String getQualifier() {
        return this.qualifier;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getDescriptor() {
        return this.descriptor;
    }

    public boolean rollbackOn(Throwable ex) {
        return (ex instanceof RuntimeException) || (ex instanceof Error);
    }

    protected final StringBuilder getAttributeDescription() {
        StringBuilder result = getDefinitionDescription();
        if (StringUtils.hasText(this.qualifier)) {
            result.append("; '").append(this.qualifier).append("'");
        }
        return result;
    }
}
