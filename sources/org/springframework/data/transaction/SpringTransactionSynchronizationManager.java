package org.springframework.data.transaction;

import org.springframework.transaction.support.TransactionSynchronizationManager;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/transaction/SpringTransactionSynchronizationManager.class */
enum SpringTransactionSynchronizationManager implements SynchronizationManager {
    INSTANCE;

    @Override // org.springframework.data.transaction.SynchronizationManager
    public void initSynchronization() throws IllegalStateException {
        TransactionSynchronizationManager.initSynchronization();
    }

    @Override // org.springframework.data.transaction.SynchronizationManager
    public boolean isSynchronizationActive() {
        return TransactionSynchronizationManager.isSynchronizationActive();
    }

    @Override // org.springframework.data.transaction.SynchronizationManager
    public void clearSynchronization() {
        TransactionSynchronizationManager.clear();
    }
}
