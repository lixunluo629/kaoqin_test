package org.springframework.data.crossstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.support.TransactionSynchronization;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/crossstore/ChangeSetBackedTransactionSynchronization.class */
public class ChangeSetBackedTransactionSynchronization implements TransactionSynchronization {
    private final ChangeSetPersister<Object> changeSetPersister;
    private final ChangeSetBacked entity;
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private int changeSetTxStatus = -1;

    public ChangeSetBackedTransactionSynchronization(ChangeSetPersister<Object> changeSetPersister, ChangeSetBacked entity) {
        this.changeSetPersister = changeSetPersister;
        this.entity = entity;
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void afterCommit() throws DataAccessException {
        this.log.debug("After Commit called for " + this.entity);
        this.changeSetPersister.persistState(this.entity, this.entity.getChangeSet());
        this.changeSetTxStatus = 0;
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void afterCompletion(int status) {
        this.log.debug("After Completion called with status = " + status);
        if (this.changeSetTxStatus == 0) {
            if (status == 0) {
                this.log.debug("ChangedSetBackedTransactionSynchronization completed successfully for " + this.entity);
            } else {
                this.log.error("ChangedSetBackedTransactionSynchronization failed for " + this.entity);
            }
        }
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void beforeCommit(boolean readOnly) {
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void beforeCompletion() {
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization, java.io.Flushable
    public void flush() {
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void resume() {
        throw new IllegalStateException("ChangedSetBackedTransactionSynchronization does not support transaction suspension currently.");
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void suspend() {
        throw new IllegalStateException("ChangedSetBackedTransactionSynchronization does not support transaction suspension currently.");
    }
}
