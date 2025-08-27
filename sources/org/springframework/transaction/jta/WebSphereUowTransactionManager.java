package org.springframework.transaction.jta;

import com.ibm.wsspi.uow.UOWAction;
import com.ibm.wsspi.uow.UOWActionException;
import com.ibm.wsspi.uow.UOWException;
import com.ibm.wsspi.uow.UOWManager;
import com.ibm.wsspi.uow.UOWManagerFactory;
import java.util.List;
import javax.naming.NamingException;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.InvalidTimeoutException;
import org.springframework.transaction.NestedTransactionNotSupportedException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.SmartTransactionObject;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronizationUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/jta/WebSphereUowTransactionManager.class */
public class WebSphereUowTransactionManager extends JtaTransactionManager implements CallbackPreferringPlatformTransactionManager {
    public static final String DEFAULT_UOW_MANAGER_NAME = "java:comp/websphere/UOWManager";
    private UOWManager uowManager;
    private String uowManagerName;

    public WebSphereUowTransactionManager() {
        setAutodetectTransactionManager(false);
    }

    public WebSphereUowTransactionManager(UOWManager uowManager) {
        this();
        this.uowManager = uowManager;
    }

    public void setUowManager(UOWManager uowManager) {
        this.uowManager = uowManager;
    }

    public void setUowManagerName(String uowManagerName) {
        this.uowManagerName = uowManagerName;
    }

    @Override // org.springframework.transaction.jta.JtaTransactionManager, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws TransactionSystemException {
        initUserTransactionAndTransactionManager();
        if (this.uowManager == null) {
            if (this.uowManagerName != null) {
                this.uowManager = lookupUowManager(this.uowManagerName);
            } else {
                this.uowManager = lookupDefaultUowManager();
            }
        }
    }

    protected UOWManager lookupUowManager(String uowManagerName) throws TransactionSystemException {
        try {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Retrieving WebSphere UOWManager from JNDI location [" + uowManagerName + "]");
            }
            return (UOWManager) getJndiTemplate().lookup(uowManagerName, UOWManager.class);
        } catch (NamingException ex) {
            throw new TransactionSystemException("WebSphere UOWManager is not available at JNDI location [" + uowManagerName + "]", ex);
        }
    }

    protected UOWManager lookupDefaultUowManager() throws TransactionSystemException {
        try {
            this.logger.debug("Retrieving WebSphere UOWManager from default JNDI location [java:comp/websphere/UOWManager]");
            return (UOWManager) getJndiTemplate().lookup(DEFAULT_UOW_MANAGER_NAME, UOWManager.class);
        } catch (NamingException e) {
            this.logger.debug("WebSphere UOWManager is not available at default JNDI location [java:comp/websphere/UOWManager] - falling back to UOWManagerFactory lookup");
            return UOWManagerFactory.getUOWManager();
        }
    }

    @Override // org.springframework.transaction.jta.JtaTransactionManager
    protected void doRegisterAfterCompletionWithJtaTransaction(JtaTransactionObject txObject, List<TransactionSynchronization> synchronizations) {
        this.uowManager.registerInterposedSynchronization(new JtaAfterCompletionSynchronization(synchronizations));
    }

    @Override // org.springframework.transaction.jta.JtaTransactionManager, org.springframework.transaction.jta.TransactionFactory
    public boolean supportsResourceAdapterManagedTransactions() {
        return true;
    }

    @Override // org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager
    public <T> T execute(TransactionDefinition definition, TransactionCallback<T> callback) throws TransactionException {
        boolean newSynch;
        if (definition == null) {
            definition = new DefaultTransactionDefinition();
        }
        if (definition.getTimeout() < -1) {
            throw new InvalidTimeoutException("Invalid transaction timeout", definition.getTimeout());
        }
        int pb = definition.getPropagationBehavior();
        boolean existingTx = (this.uowManager.getUOWStatus() == 5 || this.uowManager.getUOWType() == 0) ? false : true;
        int uowType = 1;
        boolean joinTx = false;
        if (existingTx) {
            if (pb == 5) {
                throw new IllegalTransactionStateException("Transaction propagation 'never' but existing transaction found");
            }
            if (pb == 6) {
                throw new NestedTransactionNotSupportedException("Transaction propagation 'nested' not supported for WebSphere UOW transactions");
            }
            if (pb == 1 || pb == 0 || pb == 2) {
                joinTx = true;
                newSynch = getTransactionSynchronization() != 2;
            } else if (pb == 4) {
                uowType = 0;
                newSynch = getTransactionSynchronization() == 0;
            } else {
                newSynch = getTransactionSynchronization() != 2;
            }
        } else {
            if (pb == 2) {
                throw new IllegalTransactionStateException("Transaction propagation 'mandatory' but no existing transaction found");
            }
            if (pb == 1 || pb == 4 || pb == 5) {
                uowType = 0;
                newSynch = getTransactionSynchronization() == 0;
            } else {
                newSynch = getTransactionSynchronization() != 2;
            }
        }
        boolean debug = this.logger.isDebugEnabled();
        if (debug) {
            this.logger.debug("Creating new transaction with name [" + definition.getName() + "]: " + definition);
        }
        AbstractPlatformTransactionManager.SuspendedResourcesHolder suspendedResources = !joinTx ? suspend(null) : null;
        UOWActionAdapter<T> action = null;
        try {
            try {
                if (definition.getTimeout() > -1) {
                    this.uowManager.setUOWTimeout(uowType, definition.getTimeout());
                }
                if (debug) {
                    this.logger.debug("Invoking WebSphere UOW action: type=" + uowType + ", join=" + joinTx);
                }
                action = new UOWActionAdapter<>(definition, callback, uowType == 1, !joinTx, newSynch, debug);
                this.uowManager.runUnderUOW(uowType, joinTx, action);
                if (debug) {
                    this.logger.debug("Returned from WebSphere UOW action: type=" + uowType + ", join=" + joinTx);
                }
                T result = action.getResult();
                if (suspendedResources != null) {
                    resume(null, suspendedResources);
                }
                return result;
            } catch (UOWActionException ex) {
                TransactionSystemException tse = new TransactionSystemException("UOWManager threw unexpected UOWActionException", ex);
                Throwable appEx = action.getException();
                if (appEx != null) {
                    this.logger.error("Application exception overridden by rollback exception", appEx);
                    tse.initApplicationException(appEx);
                }
                throw tse;
            } catch (UOWException ex2) {
                TransactionSystemException tse2 = new TransactionSystemException("UOWManager transaction processing failed", ex2);
                Throwable appEx2 = action.getException();
                if (appEx2 != null) {
                    this.logger.error("Application exception overridden by rollback exception", appEx2);
                    tse2.initApplicationException(appEx2);
                }
                throw tse2;
            }
        } catch (Throwable th) {
            if (suspendedResources != null) {
                resume(null, suspendedResources);
            }
            throw th;
        }
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/jta/WebSphereUowTransactionManager$UOWActionAdapter.class */
    private class UOWActionAdapter<T> implements UOWAction, SmartTransactionObject {
        private final TransactionDefinition definition;
        private final TransactionCallback<T> callback;
        private final boolean actualTransaction;
        private final boolean newTransaction;
        private final boolean newSynchronization;
        private boolean debug;
        private T result;
        private Throwable exception;

        public UOWActionAdapter(TransactionDefinition definition, TransactionCallback<T> callback, boolean actualTransaction, boolean newTransaction, boolean newSynchronization, boolean debug) {
            this.definition = definition;
            this.callback = callback;
            this.actualTransaction = actualTransaction;
            this.newTransaction = newTransaction;
            this.newSynchronization = newSynchronization;
            this.debug = debug;
        }

        public void run() throws IllegalStateException {
            DefaultTransactionStatus status = WebSphereUowTransactionManager.this.prepareTransactionStatus(this.definition, this.actualTransaction ? this : null, this.newTransaction, this.newSynchronization, this.debug, null);
            try {
                try {
                    this.result = this.callback.doInTransaction(status);
                    WebSphereUowTransactionManager.this.triggerBeforeCommit(status);
                    if (status.isLocalRollbackOnly()) {
                        if (status.isDebug()) {
                            WebSphereUowTransactionManager.this.logger.debug("Transaction callback has explicitly requested rollback");
                        }
                        WebSphereUowTransactionManager.this.uowManager.setRollbackOnly();
                    }
                    WebSphereUowTransactionManager.this.triggerBeforeCompletion(status);
                    if (status.isNewSynchronization()) {
                        List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
                        TransactionSynchronizationManager.clear();
                        if (!synchronizations.isEmpty()) {
                            WebSphereUowTransactionManager.this.uowManager.registerInterposedSynchronization(new JtaAfterCompletionSynchronization(synchronizations));
                        }
                    }
                } catch (Throwable ex) {
                    this.exception = ex;
                    if (status.isDebug()) {
                        WebSphereUowTransactionManager.this.logger.debug("Rolling back on application exception from transaction callback", ex);
                    }
                    WebSphereUowTransactionManager.this.uowManager.setRollbackOnly();
                    if (status.isLocalRollbackOnly()) {
                        if (status.isDebug()) {
                            WebSphereUowTransactionManager.this.logger.debug("Transaction callback has explicitly requested rollback");
                        }
                        WebSphereUowTransactionManager.this.uowManager.setRollbackOnly();
                    }
                    WebSphereUowTransactionManager.this.triggerBeforeCompletion(status);
                    if (status.isNewSynchronization()) {
                        List<TransactionSynchronization> synchronizations2 = TransactionSynchronizationManager.getSynchronizations();
                        TransactionSynchronizationManager.clear();
                        if (!synchronizations2.isEmpty()) {
                            WebSphereUowTransactionManager.this.uowManager.registerInterposedSynchronization(new JtaAfterCompletionSynchronization(synchronizations2));
                        }
                    }
                }
            } catch (Throwable th) {
                if (status.isLocalRollbackOnly()) {
                    if (status.isDebug()) {
                        WebSphereUowTransactionManager.this.logger.debug("Transaction callback has explicitly requested rollback");
                    }
                    WebSphereUowTransactionManager.this.uowManager.setRollbackOnly();
                }
                WebSphereUowTransactionManager.this.triggerBeforeCompletion(status);
                if (status.isNewSynchronization()) {
                    List<TransactionSynchronization> synchronizations3 = TransactionSynchronizationManager.getSynchronizations();
                    TransactionSynchronizationManager.clear();
                    if (!synchronizations3.isEmpty()) {
                        WebSphereUowTransactionManager.this.uowManager.registerInterposedSynchronization(new JtaAfterCompletionSynchronization(synchronizations3));
                    }
                }
                throw th;
            }
        }

        public T getResult() {
            if (this.exception != null) {
                ReflectionUtils.rethrowRuntimeException(this.exception);
            }
            return this.result;
        }

        public Throwable getException() {
            return this.exception;
        }

        @Override // org.springframework.transaction.support.SmartTransactionObject
        public boolean isRollbackOnly() {
            return WebSphereUowTransactionManager.this.uowManager.getRollbackOnly();
        }

        @Override // org.springframework.transaction.support.SmartTransactionObject, java.io.Flushable
        public void flush() {
            TransactionSynchronizationUtils.triggerFlush();
        }
    }
}
