package org.springframework.data.repository.core.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/SurroundingTransactionDetectorMethodInterceptor.class */
public enum SurroundingTransactionDetectorMethodInterceptor implements MethodInterceptor {
    INSTANCE;

    private final ThreadLocal<Boolean> SURROUNDING_TX_ACTIVE = new ThreadLocal<>();

    SurroundingTransactionDetectorMethodInterceptor() {
    }

    public boolean isSurroundingTransactionActive() {
        return Boolean.TRUE == this.SURROUNDING_TX_ACTIVE.get();
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation invocation) throws Throwable {
        this.SURROUNDING_TX_ACTIVE.set(Boolean.valueOf(TransactionSynchronizationManager.isActualTransactionActive()));
        try {
            return invocation.proceed();
        } finally {
            this.SURROUNDING_TX_ACTIVE.remove();
        }
    }
}
