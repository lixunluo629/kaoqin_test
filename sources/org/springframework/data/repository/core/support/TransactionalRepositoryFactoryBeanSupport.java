package org.springframework.data.repository.core.support;

import java.io.Serializable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.util.TxUtils;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/TransactionalRepositoryFactoryBeanSupport.class */
public abstract class TransactionalRepositoryFactoryBeanSupport<T extends Repository<S, ID>, S, ID extends Serializable> extends RepositoryFactoryBeanSupport<T, S, ID> implements BeanFactoryAware {
    private String transactionManagerName;
    private RepositoryProxyPostProcessor txPostProcessor;
    private RepositoryProxyPostProcessor exceptionPostProcessor;
    private boolean enableDefaultTransactions;

    protected abstract RepositoryFactorySupport doCreateRepositoryFactory();

    protected TransactionalRepositoryFactoryBeanSupport(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
        this.transactionManagerName = TxUtils.DEFAULT_TRANSACTION_MANAGER;
        this.enableDefaultTransactions = true;
    }

    public void setTransactionManager(String transactionManager) {
        this.transactionManagerName = transactionManager == null ? TxUtils.DEFAULT_TRANSACTION_MANAGER : transactionManager;
    }

    public void setEnableDefaultTransactions(boolean enableDefaultTransactions) {
        this.enableDefaultTransactions = enableDefaultTransactions;
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport
    protected final RepositoryFactorySupport createRepositoryFactory() {
        RepositoryFactorySupport factory = doCreateRepositoryFactory();
        factory.addRepositoryProxyPostProcessor(this.exceptionPostProcessor);
        factory.addRepositoryProxyPostProcessor(this.txPostProcessor);
        return factory;
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport, org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        Assert.isInstanceOf(ListableBeanFactory.class, beanFactory);
        super.setBeanFactory(beanFactory);
        ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
        this.txPostProcessor = new TransactionalRepositoryProxyPostProcessor(listableBeanFactory, this.transactionManagerName, this.enableDefaultTransactions);
        this.exceptionPostProcessor = new PersistenceExceptionTranslationRepositoryProxyPostProcessor(listableBeanFactory);
    }
}
