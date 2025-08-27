package org.springframework.transaction.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/interceptor/CompositeTransactionAttributeSource.class */
public class CompositeTransactionAttributeSource implements TransactionAttributeSource, Serializable {
    private final TransactionAttributeSource[] transactionAttributeSources;

    public CompositeTransactionAttributeSource(TransactionAttributeSource... transactionAttributeSources) {
        Assert.notNull(transactionAttributeSources, "TransactionAttributeSource array must not be null");
        this.transactionAttributeSources = transactionAttributeSources;
    }

    public final TransactionAttributeSource[] getTransactionAttributeSources() {
        return this.transactionAttributeSources;
    }

    @Override // org.springframework.transaction.interceptor.TransactionAttributeSource
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        for (TransactionAttributeSource source : this.transactionAttributeSources) {
            TransactionAttribute attr = source.getTransactionAttribute(method, targetClass);
            if (attr != null) {
                return attr;
            }
        }
        return null;
    }
}
