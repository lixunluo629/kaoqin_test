package org.springframework.transaction.support;

import java.lang.reflect.UndeclaredThrowableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/support/TransactionTemplate.class */
public class TransactionTemplate extends DefaultTransactionDefinition implements TransactionOperations, InitializingBean {
    protected final Log logger;
    private PlatformTransactionManager transactionManager;

    public TransactionTemplate() {
        this.logger = LogFactory.getLog(getClass());
    }

    public TransactionTemplate(PlatformTransactionManager transactionManager) {
        this.logger = LogFactory.getLog(getClass());
        this.transactionManager = transactionManager;
    }

    public TransactionTemplate(PlatformTransactionManager transactionManager, TransactionDefinition transactionDefinition) {
        super(transactionDefinition);
        this.logger = LogFactory.getLog(getClass());
        this.transactionManager = transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public PlatformTransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (this.transactionManager == null) {
            throw new IllegalArgumentException("Property 'transactionManager' is required");
        }
    }

    @Override // org.springframework.transaction.support.TransactionOperations
    public <T> T execute(TransactionCallback<T> transactionCallback) throws TransactionException {
        if (this.transactionManager instanceof CallbackPreferringPlatformTransactionManager) {
            return (T) ((CallbackPreferringPlatformTransactionManager) this.transactionManager).execute(this, transactionCallback);
        }
        TransactionStatus transaction = this.transactionManager.getTransaction(this);
        try {
            T tDoInTransaction = transactionCallback.doInTransaction(transaction);
            this.transactionManager.commit(transaction);
            return tDoInTransaction;
        } catch (Error e) {
            rollbackOnException(transaction, e);
            throw e;
        } catch (RuntimeException e2) {
            rollbackOnException(transaction, e2);
            throw e2;
        } catch (Throwable th) {
            rollbackOnException(transaction, th);
            throw new UndeclaredThrowableException(th, "TransactionCallback threw undeclared checked exception");
        }
    }

    private void rollbackOnException(TransactionStatus status, Throwable ex) throws TransactionException {
        this.logger.debug("Initiating transaction rollback on application exception", ex);
        try {
            this.transactionManager.rollback(status);
        } catch (Error err) {
            this.logger.error("Application exception overridden by rollback error", ex);
            throw err;
        } catch (TransactionSystemException ex2) {
            this.logger.error("Application exception overridden by rollback exception", ex);
            ex2.initApplicationException(ex);
            throw ex2;
        } catch (RuntimeException ex22) {
            this.logger.error("Application exception overridden by rollback exception", ex);
            throw ex22;
        }
    }

    @Override // org.springframework.transaction.support.DefaultTransactionDefinition
    public boolean equals(Object other) {
        return this == other || (super.equals(other) && (!(other instanceof TransactionTemplate) || getTransactionManager() == ((TransactionTemplate) other).getTransactionManager()));
    }
}
