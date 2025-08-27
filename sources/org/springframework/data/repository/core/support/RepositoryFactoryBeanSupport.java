package org.springframework.data.repository.core.support;

import java.io.Serializable;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.DefaultEvaluationContextProvider;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/RepositoryFactoryBeanSupport.class */
public abstract class RepositoryFactoryBeanSupport<T extends Repository<S, ID>, S, ID extends Serializable> implements InitializingBean, RepositoryFactoryInformation<S, ID>, FactoryBean<T>, BeanClassLoaderAware, BeanFactoryAware, ApplicationEventPublisherAware {
    private final Class<? extends T> repositoryInterface;
    private RepositoryFactorySupport factory;
    private QueryLookupStrategy.Key queryLookupStrategyKey;
    private Class<?> repositoryBaseClass;
    private Object customImplementation;
    private NamedQueries namedQueries;
    private MappingContext<?, ?> mappingContext;
    private ClassLoader classLoader;
    private BeanFactory beanFactory;
    private boolean lazyInit = false;
    private EvaluationContextProvider evaluationContextProvider = DefaultEvaluationContextProvider.INSTANCE;
    private ApplicationEventPublisher publisher;
    private T repository;
    private RepositoryMetadata repositoryMetadata;

    protected abstract RepositoryFactorySupport createRepositoryFactory();

    protected RepositoryFactoryBeanSupport(Class<? extends T> repositoryInterface) {
        Assert.notNull(repositoryInterface, "Repository interface must not be null!");
        this.repositoryInterface = repositoryInterface;
    }

    public void setRepositoryBaseClass(Class<?> repositoryBaseClass) {
        this.repositoryBaseClass = repositoryBaseClass;
    }

    public void setQueryLookupStrategyKey(QueryLookupStrategy.Key queryLookupStrategyKey) {
        this.queryLookupStrategyKey = queryLookupStrategyKey;
    }

    public void setCustomImplementation(Object customImplementation) {
        this.customImplementation = customImplementation;
    }

    public void setNamedQueries(NamedQueries namedQueries) {
        this.namedQueries = namedQueries;
    }

    protected void setMappingContext(MappingContext<?, ?> mappingContext) {
        this.mappingContext = mappingContext;
    }

    public void setEvaluationContextProvider(EvaluationContextProvider evaluationContextProvider) {
        this.evaluationContextProvider = evaluationContextProvider == null ? DefaultEvaluationContextProvider.INSTANCE : evaluationContextProvider;
    }

    public void setLazyInit(boolean lazy) {
        this.lazyInit = lazy;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override // org.springframework.context.ApplicationEventPublisherAware
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactoryInformation
    public EntityInformation<S, ID> getEntityInformation() {
        return this.factory.getEntityInformation(this.repositoryMetadata.getDomainType());
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactoryInformation
    public RepositoryInformation getRepositoryInformation() {
        return this.factory.getRepositoryInformation(this.repositoryMetadata, this.customImplementation == null ? null : this.customImplementation.getClass());
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactoryInformation
    public PersistentEntity<?, ?> getPersistentEntity() {
        if (this.mappingContext == null) {
            return null;
        }
        return this.mappingContext.getPersistentEntity(this.repositoryMetadata.getDomainType());
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactoryInformation
    public List<QueryMethod> getQueryMethods() {
        return this.factory.getQueryMethods();
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public T getObject() {
        return (T) initAndReturn();
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<? extends T> getObjectType() {
        return this.repositoryInterface;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws BeansException {
        this.factory = createRepositoryFactory();
        this.factory.setQueryLookupStrategyKey(this.queryLookupStrategyKey);
        this.factory.setNamedQueries(this.namedQueries);
        this.factory.setEvaluationContextProvider(this.evaluationContextProvider);
        this.factory.setRepositoryBaseClass(this.repositoryBaseClass);
        this.factory.setBeanClassLoader(this.classLoader);
        this.factory.setBeanFactory(this.beanFactory);
        if (this.publisher != null) {
            this.factory.addRepositoryProxyPostProcessor(new EventPublishingRepositoryProxyPostProcessor(this.publisher));
        }
        this.repositoryMetadata = this.factory.getRepositoryMetadata(this.repositoryInterface);
        if (!this.lazyInit) {
            initAndReturn();
        }
    }

    private T initAndReturn() {
        Assert.notNull(this.repositoryInterface, "Repository interface must not be null on initialization!");
        if (this.repository == null) {
            this.repository = (T) this.factory.getRepository(this.repositoryInterface, this.customImplementation);
        }
        return this.repository;
    }
}
