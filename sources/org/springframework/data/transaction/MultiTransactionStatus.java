package org.springframework.data.transaction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/transaction/MultiTransactionStatus.class */
class MultiTransactionStatus implements TransactionStatus {
    private final PlatformTransactionManager mainTransactionManager;
    private final Map<PlatformTransactionManager, TransactionStatus> transactionStatuses = Collections.synchronizedMap(new HashMap());
    private boolean newSynchonization;

    public MultiTransactionStatus(PlatformTransactionManager mainTransactionManager) {
        Assert.notNull(mainTransactionManager, "TransactionManager must not be null!");
        this.mainTransactionManager = mainTransactionManager;
    }

    public Map<PlatformTransactionManager, TransactionStatus> getTransactionStatuses() {
        return this.transactionStatuses;
    }

    public void setNewSynchonization() {
        this.newSynchonization = true;
    }

    public boolean isNewSynchonization() {
        return this.newSynchonization;
    }

    public void registerTransactionManager(TransactionDefinition definition, PlatformTransactionManager transactionManager) {
        getTransactionStatuses().put(transactionManager, transactionManager.getTransaction(definition));
    }

    public void commit(PlatformTransactionManager transactionManager) throws TransactionException {
        TransactionStatus transactionStatus = getTransactionStatus(transactionManager);
        transactionManager.commit(transactionStatus);
    }

    public void rollback(PlatformTransactionManager transactionManager) throws TransactionException {
        transactionManager.rollback(getTransactionStatus(transactionManager));
    }

    @Override // org.springframework.transaction.TransactionStatus
    public boolean isRollbackOnly() {
        return getMainTransactionStatus().isRollbackOnly();
    }

    @Override // org.springframework.transaction.TransactionStatus
    public boolean isCompleted() {
        return getMainTransactionStatus().isCompleted();
    }

    @Override // org.springframework.transaction.TransactionStatus
    public boolean isNewTransaction() {
        return getMainTransactionStatus().isNewTransaction();
    }

    @Override // org.springframework.transaction.TransactionStatus
    public boolean hasSavepoint() {
        return getMainTransactionStatus().hasSavepoint();
    }

    @Override // org.springframework.transaction.TransactionStatus
    public void setRollbackOnly() {
        for (TransactionStatus ts : this.transactionStatuses.values()) {
            ts.setRollbackOnly();
        }
    }

    @Override // org.springframework.transaction.SavepointManager
    public Object createSavepoint() throws TransactionException {
        SavePoints savePoints = new SavePoints();
        for (TransactionStatus transactionStatus : this.transactionStatuses.values()) {
            savePoints.save(transactionStatus);
        }
        return savePoints;
    }

    @Override // org.springframework.transaction.SavepointManager
    public void rollbackToSavepoint(Object savepoint) throws TransactionException {
        SavePoints savePoints = (SavePoints) savepoint;
        savePoints.rollback();
    }

    @Override // org.springframework.transaction.SavepointManager
    public void releaseSavepoint(Object savepoint) throws TransactionException {
        ((SavePoints) savepoint).release();
    }

    @Override // org.springframework.transaction.TransactionStatus, java.io.Flushable
    public void flush() {
        for (TransactionStatus transactionStatus : this.transactionStatuses.values()) {
            transactionStatus.flush();
        }
    }

    private TransactionStatus getMainTransactionStatus() {
        return this.transactionStatuses.get(this.mainTransactionManager);
    }

    private TransactionStatus getTransactionStatus(PlatformTransactionManager transactionManager) {
        return getTransactionStatuses().get(transactionManager);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/transaction/MultiTransactionStatus$SavePoints.class */
    private static class SavePoints {
        private final Map<TransactionStatus, Object> savepoints;

        private SavePoints() {
            this.savepoints = new HashMap();
        }

        private void addSavePoint(TransactionStatus status, Object savepoint) {
            Assert.notNull(status, "TransactionStatus must not be null!");
            this.savepoints.put(status, savepoint);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void save(TransactionStatus transactionStatus) {
            Object savepoint = transactionStatus.createSavepoint();
            addSavePoint(transactionStatus, savepoint);
        }

        public void rollback() {
            for (TransactionStatus transactionStatus : this.savepoints.keySet()) {
                transactionStatus.rollbackToSavepoint(savepointFor(transactionStatus));
            }
        }

        private Object savepointFor(TransactionStatus transactionStatus) {
            return this.savepoints.get(transactionStatus);
        }

        public void release() {
            for (TransactionStatus transactionStatus : this.savepoints.keySet()) {
                transactionStatus.releaseSavepoint(savepointFor(transactionStatus));
            }
        }
    }
}
