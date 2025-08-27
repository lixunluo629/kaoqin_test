package org.springframework.transaction.jta;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/jta/UserTransactionAdapter.class */
public class UserTransactionAdapter implements UserTransaction {
    private final TransactionManager transactionManager;

    public UserTransactionAdapter(TransactionManager transactionManager) {
        Assert.notNull(transactionManager, "TransactionManager must not be null");
        this.transactionManager = transactionManager;
    }

    public final TransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    public void setTransactionTimeout(int timeout) throws SystemException {
        this.transactionManager.setTransactionTimeout(timeout);
    }

    public void begin() throws NotSupportedException, SystemException {
        this.transactionManager.begin();
    }

    public void commit() throws SystemException, RollbackException, HeuristicRollbackException, HeuristicMixedException, SecurityException {
        this.transactionManager.commit();
    }

    public void rollback() throws SystemException, SecurityException {
        this.transactionManager.rollback();
    }

    public void setRollbackOnly() throws SystemException {
        this.transactionManager.setRollbackOnly();
    }

    public int getStatus() throws SystemException {
        return this.transactionManager.getStatus();
    }
}
