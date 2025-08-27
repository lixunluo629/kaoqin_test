package org.springframework.jca.endpoint;

import java.lang.reflect.Method;
import javax.resource.ResourceException;
import javax.resource.spi.ApplicationServerInternalException;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.transaction.jta.SimpleTransactionFactory;
import org.springframework.transaction.jta.TransactionFactory;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/endpoint/AbstractMessageEndpointFactory.class */
public abstract class AbstractMessageEndpointFactory implements MessageEndpointFactory, BeanNameAware {
    private TransactionFactory transactionFactory;
    private String transactionName;
    private String beanName;
    protected final Log logger = LogFactory.getLog(getClass());
    private int transactionTimeout = -1;

    protected abstract AbstractMessageEndpoint createEndpointInternal() throws UnavailableException;

    public void setTransactionManager(Object transactionManager) {
        if (transactionManager instanceof TransactionFactory) {
            this.transactionFactory = (TransactionFactory) transactionManager;
        } else {
            if (transactionManager instanceof TransactionManager) {
                this.transactionFactory = new SimpleTransactionFactory((TransactionManager) transactionManager);
                return;
            }
            throw new IllegalArgumentException("Transaction manager [" + transactionManager + "] is neither a [org.springframework.transaction.jta.TransactionFactory} nor a [javax.transaction.TransactionManager]");
        }
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public void setTransactionTimeout(int transactionTimeout) {
        this.transactionTimeout = transactionTimeout;
    }

    @Override // org.springframework.beans.factory.BeanNameAware
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getActivationName() {
        return this.beanName;
    }

    public boolean isDeliveryTransacted(Method method) throws NoSuchMethodException {
        return this.transactionFactory != null;
    }

    public MessageEndpoint createEndpoint(XAResource xaResource) throws UnavailableException {
        AbstractMessageEndpoint endpoint = createEndpointInternal();
        endpoint.initXAResource(xaResource);
        return endpoint;
    }

    public MessageEndpoint createEndpoint(XAResource xaResource, long timeout) throws UnavailableException {
        AbstractMessageEndpoint endpoint = createEndpointInternal();
        endpoint.initXAResource(xaResource);
        return endpoint;
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/endpoint/AbstractMessageEndpointFactory$AbstractMessageEndpoint.class */
    protected abstract class AbstractMessageEndpoint implements MessageEndpoint {
        private TransactionDelegate transactionDelegate;
        private boolean beforeDeliveryCalled = false;
        private ClassLoader previousContextClassLoader;

        protected abstract ClassLoader getEndpointClassLoader();

        protected AbstractMessageEndpoint() {
        }

        void initXAResource(XAResource xaResource) {
            this.transactionDelegate = AbstractMessageEndpointFactory.this.new TransactionDelegate(xaResource);
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: javax.resource.spi.ApplicationServerInternalException */
        public void beforeDelivery(Method method) throws ResourceException, ApplicationServerInternalException {
            this.beforeDeliveryCalled = true;
            try {
                this.transactionDelegate.beginTransaction();
                Thread currentThread = Thread.currentThread();
                this.previousContextClassLoader = currentThread.getContextClassLoader();
                currentThread.setContextClassLoader(getEndpointClassLoader());
            } catch (Throwable ex) {
                throw new ApplicationServerInternalException("Failed to begin transaction", ex);
            }
        }

        protected final boolean hasBeforeDeliveryBeenCalled() {
            return this.beforeDeliveryCalled;
        }

        protected final void onEndpointException(Throwable ex) {
            this.transactionDelegate.setRollbackOnly();
            AbstractMessageEndpointFactory.this.logger.debug("Transaction marked as rollback-only after endpoint exception", ex);
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: javax.resource.spi.ApplicationServerInternalException */
        public void afterDelivery() throws ResourceException, ApplicationServerInternalException {
            this.beforeDeliveryCalled = false;
            Thread.currentThread().setContextClassLoader(this.previousContextClassLoader);
            this.previousContextClassLoader = null;
            try {
                this.transactionDelegate.endTransaction();
            } catch (Throwable ex) {
                AbstractMessageEndpointFactory.this.logger.warn("Failed to complete transaction after endpoint delivery", ex);
                throw new ApplicationServerInternalException("Failed to complete transaction", ex);
            }
        }

        public void release() {
            try {
                this.transactionDelegate.setRollbackOnly();
                this.transactionDelegate.endTransaction();
            } catch (Throwable ex) {
                AbstractMessageEndpointFactory.this.logger.warn("Could not complete unfinished transaction on endpoint release", ex);
            }
        }
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/endpoint/AbstractMessageEndpointFactory$TransactionDelegate.class */
    private class TransactionDelegate {
        private final XAResource xaResource;
        private Transaction transaction;
        private boolean rollbackOnly;

        public TransactionDelegate(XAResource xaResource) {
            if (xaResource == null && AbstractMessageEndpointFactory.this.transactionFactory != null && !AbstractMessageEndpointFactory.this.transactionFactory.supportsResourceAdapterManagedTransactions()) {
                throw new IllegalStateException("ResourceAdapter-provided XAResource is required for transaction management. Check your ResourceAdapter's configuration.");
            }
            this.xaResource = xaResource;
        }

        public void beginTransaction() throws Exception {
            if (AbstractMessageEndpointFactory.this.transactionFactory != null && this.xaResource != null) {
                this.transaction = AbstractMessageEndpointFactory.this.transactionFactory.createTransaction(AbstractMessageEndpointFactory.this.transactionName, AbstractMessageEndpointFactory.this.transactionTimeout);
                this.transaction.enlistResource(this.xaResource);
            }
        }

        public void setRollbackOnly() {
            if (this.transaction != null) {
                this.rollbackOnly = true;
            }
        }

        public void endTransaction() throws Exception {
            if (this.transaction != null) {
                try {
                    if (this.rollbackOnly) {
                        this.transaction.rollback();
                    } else {
                        this.transaction.commit();
                    }
                } finally {
                    this.transaction = null;
                    this.rollbackOnly = false;
                }
            }
        }
    }
}
