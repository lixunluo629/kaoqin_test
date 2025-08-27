package org.springframework.transaction.jta;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/jta/WebLogicJtaTransactionManager.class */
public class WebLogicJtaTransactionManager extends JtaTransactionManager {
    private static final String USER_TRANSACTION_CLASS_NAME = "weblogic.transaction.UserTransaction";
    private static final String CLIENT_TRANSACTION_MANAGER_CLASS_NAME = "weblogic.transaction.ClientTransactionManager";
    private static final String TRANSACTION_CLASS_NAME = "weblogic.transaction.Transaction";
    private static final String TRANSACTION_HELPER_CLASS_NAME = "weblogic.transaction.TransactionHelper";
    private static final String ISOLATION_LEVEL_KEY = "ISOLATION LEVEL";
    private boolean weblogicUserTransactionAvailable;
    private Method beginWithNameMethod;
    private Method beginWithNameAndTimeoutMethod;
    private boolean weblogicTransactionManagerAvailable;
    private Method forceResumeMethod;
    private Method setPropertyMethod;
    private Object transactionHelper;

    @Override // org.springframework.transaction.jta.JtaTransactionManager, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws IllegalStateException, TransactionSystemException, ClassNotFoundException {
        super.afterPropertiesSet();
        loadWebLogicTransactionClasses();
    }

    @Override // org.springframework.transaction.jta.JtaTransactionManager
    protected UserTransaction retrieveUserTransaction() throws TransactionSystemException, NoSuchMethodException, ClassNotFoundException, SecurityException {
        loadWebLogicTransactionHelper();
        try {
            this.logger.debug("Retrieving JTA UserTransaction from WebLogic TransactionHelper");
            Method getUserTransactionMethod = this.transactionHelper.getClass().getMethod("getUserTransaction", new Class[0]);
            return (UserTransaction) getUserTransactionMethod.invoke(this.transactionHelper, new Object[0]);
        } catch (InvocationTargetException ex) {
            throw new TransactionSystemException("WebLogic's TransactionHelper.getUserTransaction() method failed", ex.getTargetException());
        } catch (Exception ex2) {
            throw new TransactionSystemException("Could not invoke WebLogic's TransactionHelper.getUserTransaction() method", ex2);
        }
    }

    @Override // org.springframework.transaction.jta.JtaTransactionManager
    protected TransactionManager retrieveTransactionManager() throws TransactionSystemException, NoSuchMethodException, ClassNotFoundException, SecurityException {
        loadWebLogicTransactionHelper();
        try {
            this.logger.debug("Retrieving JTA TransactionManager from WebLogic TransactionHelper");
            Method getTransactionManagerMethod = this.transactionHelper.getClass().getMethod("getTransactionManager", new Class[0]);
            return (TransactionManager) getTransactionManagerMethod.invoke(this.transactionHelper, new Object[0]);
        } catch (InvocationTargetException ex) {
            throw new TransactionSystemException("WebLogic's TransactionHelper.getTransactionManager() method failed", ex.getTargetException());
        } catch (Exception ex2) {
            throw new TransactionSystemException("Could not invoke WebLogic's TransactionHelper.getTransactionManager() method", ex2);
        }
    }

    private void loadWebLogicTransactionHelper() throws TransactionSystemException, NoSuchMethodException, ClassNotFoundException, SecurityException {
        if (this.transactionHelper == null) {
            try {
                Class<?> transactionHelperClass = getClass().getClassLoader().loadClass(TRANSACTION_HELPER_CLASS_NAME);
                Method getTransactionHelperMethod = transactionHelperClass.getMethod("getTransactionHelper", new Class[0]);
                this.transactionHelper = getTransactionHelperMethod.invoke(null, new Object[0]);
                this.logger.debug("WebLogic TransactionHelper found");
            } catch (InvocationTargetException ex) {
                throw new TransactionSystemException("WebLogic's TransactionHelper.getTransactionHelper() method failed", ex.getTargetException());
            } catch (Exception ex2) {
                throw new TransactionSystemException("Could not initialize WebLogicJtaTransactionManager because WebLogic API classes are not available", ex2);
            }
        }
    }

    private void loadWebLogicTransactionClasses() throws TransactionSystemException, ClassNotFoundException {
        try {
            Class<?> userTransactionClass = getClass().getClassLoader().loadClass(USER_TRANSACTION_CLASS_NAME);
            this.weblogicUserTransactionAvailable = userTransactionClass.isInstance(getUserTransaction());
            if (this.weblogicUserTransactionAvailable) {
                this.beginWithNameMethod = userTransactionClass.getMethod("begin", String.class);
                this.beginWithNameAndTimeoutMethod = userTransactionClass.getMethod("begin", String.class, Integer.TYPE);
                this.logger.info("Support for WebLogic transaction names available");
            } else {
                this.logger.info("Support for WebLogic transaction names not available");
            }
            Class<?> transactionManagerClass = getClass().getClassLoader().loadClass(CLIENT_TRANSACTION_MANAGER_CLASS_NAME);
            this.logger.debug("WebLogic ClientTransactionManager found");
            this.weblogicTransactionManagerAvailable = transactionManagerClass.isInstance(getTransactionManager());
            if (this.weblogicTransactionManagerAvailable) {
                Class<?> transactionClass = getClass().getClassLoader().loadClass(TRANSACTION_CLASS_NAME);
                this.forceResumeMethod = transactionManagerClass.getMethod("forceResume", Transaction.class);
                this.setPropertyMethod = transactionClass.getMethod("setProperty", String.class, Serializable.class);
                this.logger.debug("Support for WebLogic forceResume available");
            } else {
                this.logger.warn("Support for WebLogic forceResume not available");
            }
        } catch (Exception ex) {
            throw new TransactionSystemException("Could not initialize WebLogicJtaTransactionManager because WebLogic API classes are not available", ex);
        }
    }

    @Override // org.springframework.transaction.jta.JtaTransactionManager
    protected void doJtaBegin(JtaTransactionObject txObject, TransactionDefinition definition) throws NotSupportedException, IllegalAccessException, SystemException, IllegalArgumentException, InvocationTargetException {
        int timeout = determineTimeout(definition);
        if (this.weblogicUserTransactionAvailable && definition.getName() != null) {
            try {
                if (timeout > -1) {
                    this.beginWithNameAndTimeoutMethod.invoke(txObject.getUserTransaction(), definition.getName(), Integer.valueOf(timeout));
                } else {
                    this.beginWithNameMethod.invoke(txObject.getUserTransaction(), definition.getName());
                }
            } catch (InvocationTargetException ex) {
                throw new TransactionSystemException("WebLogic's UserTransaction.begin() method failed", ex.getTargetException());
            } catch (Exception ex2) {
                throw new TransactionSystemException("Could not invoke WebLogic's UserTransaction.begin() method", ex2);
            }
        } else {
            applyTimeout(txObject, timeout);
            txObject.getUserTransaction().begin();
        }
        if (this.weblogicTransactionManagerAvailable) {
            if (definition.getIsolationLevel() != -1) {
                try {
                    Transaction tx = getTransactionManager().getTransaction();
                    Integer isolationLevel = Integer.valueOf(definition.getIsolationLevel());
                    this.setPropertyMethod.invoke(tx, ISOLATION_LEVEL_KEY, isolationLevel);
                    return;
                } catch (InvocationTargetException ex3) {
                    throw new TransactionSystemException("WebLogic's Transaction.setProperty(String, Serializable) method failed", ex3.getTargetException());
                } catch (Exception ex4) {
                    throw new TransactionSystemException("Could not invoke WebLogic's Transaction.setProperty(String, Serializable) method", ex4);
                }
            }
            return;
        }
        applyIsolationLevel(txObject, definition.getIsolationLevel());
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.transaction.InvalidTransactionException */
    @Override // org.springframework.transaction.jta.JtaTransactionManager
    protected void doJtaResume(JtaTransactionObject txObject, Object suspendedTransaction) throws IllegalAccessException, SystemException, InvalidTransactionException, IllegalArgumentException, InvocationTargetException {
        try {
            getTransactionManager().resume((Transaction) suspendedTransaction);
        } catch (InvalidTransactionException ex) {
            if (!this.weblogicTransactionManagerAvailable) {
                throw ex;
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Standard JTA resume threw InvalidTransactionException: " + ex.getMessage() + " - trying WebLogic JTA forceResume");
            }
            try {
                this.forceResumeMethod.invoke(getTransactionManager(), suspendedTransaction);
            } catch (InvocationTargetException ex2) {
                throw new TransactionSystemException("WebLogic's TransactionManager.forceResume(Transaction) method failed", ex2.getTargetException());
            } catch (Exception ex22) {
                throw new TransactionSystemException("Could not access WebLogic's TransactionManager.forceResume(Transaction) method", ex22);
            }
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.transaction.NotSupportedException */
    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.transaction.SystemException */
    @Override // org.springframework.transaction.jta.JtaTransactionManager, org.springframework.transaction.jta.TransactionFactory
    public Transaction createTransaction(String name, int timeout) throws NotSupportedException, IllegalAccessException, SystemException, IllegalArgumentException, InvocationTargetException {
        if (this.weblogicUserTransactionAvailable && name != null) {
            try {
                if (timeout >= 0) {
                    this.beginWithNameAndTimeoutMethod.invoke(getUserTransaction(), name, Integer.valueOf(timeout));
                } else {
                    this.beginWithNameMethod.invoke(getUserTransaction(), name);
                }
                return new ManagedTransactionAdapter(getTransactionManager());
            } catch (InvocationTargetException ex) {
                if (ex.getTargetException() instanceof NotSupportedException) {
                    throw ex.getTargetException();
                }
                if (ex.getTargetException() instanceof SystemException) {
                    throw ex.getTargetException();
                }
                if (ex.getTargetException() instanceof RuntimeException) {
                    throw ((RuntimeException) ex.getTargetException());
                }
                throw new SystemException("WebLogic's begin() method failed with an unexpected error: " + ex.getTargetException());
            } catch (Exception ex2) {
                throw new SystemException("Could not invoke WebLogic's UserTransaction.begin() method: " + ex2);
            }
        }
        return super.createTransaction(name, timeout);
    }
}
