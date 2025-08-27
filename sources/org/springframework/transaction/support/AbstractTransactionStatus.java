package org.springframework.transaction.support;

import org.springframework.transaction.NestedTransactionNotSupportedException;
import org.springframework.transaction.SavepointManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionUsageException;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/support/AbstractTransactionStatus.class */
public abstract class AbstractTransactionStatus implements TransactionStatus {
    private boolean rollbackOnly = false;
    private boolean completed = false;
    private Object savepoint;

    @Override // org.springframework.transaction.TransactionStatus
    public void setRollbackOnly() {
        this.rollbackOnly = true;
    }

    @Override // org.springframework.transaction.TransactionStatus
    public boolean isRollbackOnly() {
        return isLocalRollbackOnly() || isGlobalRollbackOnly();
    }

    public boolean isLocalRollbackOnly() {
        return this.rollbackOnly;
    }

    public boolean isGlobalRollbackOnly() {
        return false;
    }

    @Override // org.springframework.transaction.TransactionStatus, java.io.Flushable
    public void flush() {
    }

    public void setCompleted() {
        this.completed = true;
    }

    @Override // org.springframework.transaction.TransactionStatus
    public boolean isCompleted() {
        return this.completed;
    }

    protected void setSavepoint(Object savepoint) {
        this.savepoint = savepoint;
    }

    protected Object getSavepoint() {
        return this.savepoint;
    }

    @Override // org.springframework.transaction.TransactionStatus
    public boolean hasSavepoint() {
        return this.savepoint != null;
    }

    public void createAndHoldSavepoint() throws TransactionException {
        setSavepoint(getSavepointManager().createSavepoint());
    }

    public void rollbackToHeldSavepoint() throws TransactionException {
        if (!hasSavepoint()) {
            throw new TransactionUsageException("Cannot roll back to savepoint - no savepoint associated with current transaction");
        }
        getSavepointManager().rollbackToSavepoint(getSavepoint());
        getSavepointManager().releaseSavepoint(getSavepoint());
        setSavepoint(null);
    }

    public void releaseHeldSavepoint() throws TransactionException {
        if (!hasSavepoint()) {
            throw new TransactionUsageException("Cannot release savepoint - no savepoint associated with current transaction");
        }
        getSavepointManager().releaseSavepoint(getSavepoint());
        setSavepoint(null);
    }

    @Override // org.springframework.transaction.SavepointManager
    public Object createSavepoint() throws TransactionException {
        return getSavepointManager().createSavepoint();
    }

    @Override // org.springframework.transaction.SavepointManager
    public void rollbackToSavepoint(Object savepoint) throws TransactionException {
        getSavepointManager().rollbackToSavepoint(savepoint);
    }

    @Override // org.springframework.transaction.SavepointManager
    public void releaseSavepoint(Object savepoint) throws TransactionException {
        getSavepointManager().releaseSavepoint(savepoint);
    }

    protected SavepointManager getSavepointManager() {
        throw new NestedTransactionNotSupportedException("This transaction does not support savepoints");
    }
}
