package org.springframework.transaction.interceptor;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAspectSupport.class */
public abstract class TransactionAspectSupport implements BeanFactoryAware, InitializingBean {
    private static final Object DEFAULT_TRANSACTION_MANAGER_KEY = new Object();
    private static final ThreadLocal<TransactionInfo> transactionInfoHolder = new NamedThreadLocal("Current aspect-driven transaction");
    private String transactionManagerBeanName;
    private PlatformTransactionManager transactionManager;
    private TransactionAttributeSource transactionAttributeSource;
    private BeanFactory beanFactory;
    protected final Log logger = LogFactory.getLog(getClass());
    private final ConcurrentMap<Object, PlatformTransactionManager> transactionManagerCache = new ConcurrentReferenceHashMap(4);

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAspectSupport$InvocationCallback.class */
    protected interface InvocationCallback {
        Object proceedWithInvocation() throws Throwable;
    }

    protected static TransactionInfo currentTransactionInfo() throws NoTransactionException {
        return transactionInfoHolder.get();
    }

    public static TransactionStatus currentTransactionStatus() throws NoTransactionException {
        TransactionInfo info = currentTransactionInfo();
        if (info == null || info.transactionStatus == null) {
            throw new NoTransactionException("No transaction aspect-managed TransactionStatus in scope");
        }
        return info.transactionStatus;
    }

    public void setTransactionManagerBeanName(String transactionManagerBeanName) {
        this.transactionManagerBeanName = transactionManagerBeanName;
    }

    protected final String getTransactionManagerBeanName() {
        return this.transactionManagerBeanName;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public PlatformTransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    public void setTransactionAttributes(Properties transactionAttributes) throws IllegalArgumentException {
        NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
        tas.setProperties(transactionAttributes);
        this.transactionAttributeSource = tas;
    }

    public void setTransactionAttributeSources(TransactionAttributeSource... transactionAttributeSources) {
        this.transactionAttributeSource = new CompositeTransactionAttributeSource(transactionAttributeSources);
    }

    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
        this.transactionAttributeSource = transactionAttributeSource;
    }

    public TransactionAttributeSource getTransactionAttributeSource() {
        return this.transactionAttributeSource;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    protected final BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (getTransactionManager() == null && this.beanFactory == null) {
            throw new IllegalStateException("Set the 'transactionManager' property or make sure to run within a BeanFactory containing a PlatformTransactionManager bean!");
        }
        if (getTransactionAttributeSource() == null) {
            throw new IllegalStateException("Either 'transactionAttributeSource' or 'transactionAttributes' is required: If there are no transactional methods, then don't use a transaction aspect.");
        }
    }

    protected Object invokeWithinTransaction(Method method, Class<?> targetClass, final InvocationCallback invocation) throws Throwable {
        final TransactionAttribute txAttr = getTransactionAttributeSource().getTransactionAttribute(method, targetClass);
        final PlatformTransactionManager tm = determineTransactionManager(txAttr);
        final String joinpointIdentification = methodIdentification(method, targetClass, txAttr);
        if (txAttr == null || !(tm instanceof CallbackPreferringPlatformTransactionManager)) {
            TransactionInfo txInfo = createTransactionIfNecessary(tm, txAttr, joinpointIdentification);
            try {
                try {
                    Object retVal = invocation.proceedWithInvocation();
                    cleanupTransactionInfo(txInfo);
                    commitTransactionAfterReturning(txInfo);
                    return retVal;
                } catch (Throwable ex) {
                    completeTransactionAfterThrowing(txInfo, ex);
                    throw ex;
                }
            } catch (Throwable th) {
                cleanupTransactionInfo(txInfo);
                throw th;
            }
        }
        final ThrowableHolder throwableHolder = new ThrowableHolder();
        try {
            Object result = ((CallbackPreferringPlatformTransactionManager) tm).execute(txAttr, new TransactionCallback<Object>() { // from class: org.springframework.transaction.interceptor.TransactionAspectSupport.1
                @Override // org.springframework.transaction.support.TransactionCallback
                public Object doInTransaction(TransactionStatus status) {
                    TransactionInfo txInfo2 = TransactionAspectSupport.this.prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
                    try {
                        try {
                            Object objProceedWithInvocation = invocation.proceedWithInvocation();
                            TransactionAspectSupport.this.cleanupTransactionInfo(txInfo2);
                            return objProceedWithInvocation;
                        } catch (Throwable ex2) {
                            if (txAttr.rollbackOn(ex2)) {
                                if (ex2 instanceof RuntimeException) {
                                    throw ((RuntimeException) ex2);
                                }
                                throw new ThrowableHolderException(ex2);
                            }
                            throwableHolder.throwable = ex2;
                            TransactionAspectSupport.this.cleanupTransactionInfo(txInfo2);
                            return null;
                        }
                    } catch (Throwable th2) {
                        TransactionAspectSupport.this.cleanupTransactionInfo(txInfo2);
                        throw th2;
                    }
                }
            });
            if (throwableHolder.throwable != null) {
                throw throwableHolder.throwable;
            }
            return result;
        } catch (TransactionSystemException ex2) {
            if (throwableHolder.throwable != null) {
                this.logger.error("Application exception overridden by commit exception", throwableHolder.throwable);
                ex2.initApplicationException(throwableHolder.throwable);
            }
            throw ex2;
        } catch (ThrowableHolderException ex3) {
            throw ex3.getCause();
        } catch (Throwable ex22) {
            if (throwableHolder.throwable != null) {
                this.logger.error("Application exception overridden by commit exception", throwableHolder.throwable);
            }
            throw ex22;
        }
    }

    protected void clearTransactionManagerCache() {
        this.transactionManagerCache.clear();
        this.beanFactory = null;
    }

    protected PlatformTransactionManager determineTransactionManager(TransactionAttribute txAttr) {
        if (txAttr == null || this.beanFactory == null) {
            return getTransactionManager();
        }
        String qualifier = txAttr.getQualifier();
        if (StringUtils.hasText(qualifier)) {
            return determineQualifiedTransactionManager(qualifier);
        }
        if (StringUtils.hasText(this.transactionManagerBeanName)) {
            return determineQualifiedTransactionManager(this.transactionManagerBeanName);
        }
        PlatformTransactionManager defaultTransactionManager = getTransactionManager();
        if (defaultTransactionManager == null) {
            defaultTransactionManager = this.transactionManagerCache.get(DEFAULT_TRANSACTION_MANAGER_KEY);
            if (defaultTransactionManager == null) {
                defaultTransactionManager = (PlatformTransactionManager) this.beanFactory.getBean(PlatformTransactionManager.class);
                this.transactionManagerCache.putIfAbsent(DEFAULT_TRANSACTION_MANAGER_KEY, defaultTransactionManager);
            }
        }
        return defaultTransactionManager;
    }

    private PlatformTransactionManager determineQualifiedTransactionManager(String qualifier) {
        PlatformTransactionManager txManager = this.transactionManagerCache.get(qualifier);
        if (txManager == null) {
            txManager = (PlatformTransactionManager) BeanFactoryAnnotationUtils.qualifiedBeanOfType(this.beanFactory, PlatformTransactionManager.class, qualifier);
            this.transactionManagerCache.putIfAbsent(qualifier, txManager);
        }
        return txManager;
    }

    private String methodIdentification(Method method, Class<?> targetClass, TransactionAttribute txAttr) {
        String methodIdentification = methodIdentification(method, targetClass);
        if (methodIdentification == null) {
            if (txAttr instanceof DefaultTransactionAttribute) {
                methodIdentification = ((DefaultTransactionAttribute) txAttr).getDescriptor();
            }
            if (methodIdentification == null) {
                methodIdentification = ClassUtils.getQualifiedMethodName(method, targetClass);
            }
        }
        return methodIdentification;
    }

    protected String methodIdentification(Method method, Class<?> targetClass) {
        return null;
    }

    protected TransactionInfo createTransactionIfNecessary(PlatformTransactionManager tm, TransactionAttribute txAttr, final String joinpointIdentification) throws TransactionException {
        if (txAttr != null && txAttr.getName() == null) {
            txAttr = new DelegatingTransactionAttribute(txAttr) { // from class: org.springframework.transaction.interceptor.TransactionAspectSupport.2
                @Override // org.springframework.transaction.support.DelegatingTransactionDefinition, org.springframework.transaction.TransactionDefinition
                public String getName() {
                    return joinpointIdentification;
                }
            };
        }
        TransactionStatus status = null;
        if (txAttr != null) {
            if (tm != null) {
                status = tm.getTransaction(txAttr);
            } else if (this.logger.isDebugEnabled()) {
                this.logger.debug("Skipping transactional joinpoint [" + joinpointIdentification + "] because no transaction manager has been configured");
            }
        }
        return prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
    }

    protected TransactionInfo prepareTransactionInfo(PlatformTransactionManager tm, TransactionAttribute txAttr, String joinpointIdentification, TransactionStatus status) {
        TransactionInfo txInfo = new TransactionInfo(tm, txAttr, joinpointIdentification);
        if (txAttr != null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Getting transaction for [" + txInfo.getJoinpointIdentification() + "]");
            }
            txInfo.newTransactionStatus(status);
        } else if (this.logger.isTraceEnabled()) {
            this.logger.trace("No need to create transaction for [" + joinpointIdentification + "]: This method is not transactional.");
        }
        txInfo.bindToThread();
        return txInfo;
    }

    protected void commitTransactionAfterReturning(TransactionInfo txInfo) throws TransactionException {
        if (txInfo != null && txInfo.hasTransaction()) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() + "]");
            }
            txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
        }
    }

    protected void completeTransactionAfterThrowing(TransactionInfo txInfo, Throwable ex) {
        if (txInfo != null && txInfo.hasTransaction()) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() + "] after exception: " + ex);
            }
            if (txInfo.transactionAttribute.rollbackOn(ex)) {
                try {
                    txInfo.getTransactionManager().rollback(txInfo.getTransactionStatus());
                    return;
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
            try {
                txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
            } catch (Error err2) {
                this.logger.error("Application exception overridden by commit error", ex);
                throw err2;
            } catch (TransactionSystemException ex23) {
                this.logger.error("Application exception overridden by commit exception", ex);
                ex23.initApplicationException(ex);
                throw ex23;
            } catch (RuntimeException ex24) {
                this.logger.error("Application exception overridden by commit exception", ex);
                throw ex24;
            }
        }
    }

    protected void cleanupTransactionInfo(TransactionInfo txInfo) {
        if (txInfo == null) {
            return;
        }
        txInfo.restoreThreadLocalStatus();
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAspectSupport$TransactionInfo.class */
    protected final class TransactionInfo {
        private final PlatformTransactionManager transactionManager;
        private final TransactionAttribute transactionAttribute;
        private final String joinpointIdentification;
        private TransactionStatus transactionStatus;
        private TransactionInfo oldTransactionInfo;

        public TransactionInfo(PlatformTransactionManager transactionManager, TransactionAttribute transactionAttribute, String joinpointIdentification) {
            this.transactionManager = transactionManager;
            this.transactionAttribute = transactionAttribute;
            this.joinpointIdentification = joinpointIdentification;
        }

        public PlatformTransactionManager getTransactionManager() {
            return this.transactionManager;
        }

        public TransactionAttribute getTransactionAttribute() {
            return this.transactionAttribute;
        }

        public String getJoinpointIdentification() {
            return this.joinpointIdentification;
        }

        public void newTransactionStatus(TransactionStatus status) {
            this.transactionStatus = status;
        }

        public TransactionStatus getTransactionStatus() {
            return this.transactionStatus;
        }

        public boolean hasTransaction() {
            return this.transactionStatus != null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void bindToThread() {
            this.oldTransactionInfo = (TransactionInfo) TransactionAspectSupport.transactionInfoHolder.get();
            TransactionAspectSupport.transactionInfoHolder.set(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void restoreThreadLocalStatus() {
            TransactionAspectSupport.transactionInfoHolder.set(this.oldTransactionInfo);
        }

        public String toString() {
            return this.transactionAttribute.toString();
        }
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAspectSupport$ThrowableHolder.class */
    private static class ThrowableHolder {
        public Throwable throwable;

        private ThrowableHolder() {
        }
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAspectSupport$ThrowableHolderException.class */
    private static class ThrowableHolderException extends RuntimeException {
        public ThrowableHolderException(Throwable throwable) {
            super(throwable);
        }

        @Override // java.lang.Throwable
        public String toString() {
            return getCause().toString();
        }
    }
}
