package org.springframework.data.repository.support;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.util.ProxyUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/Repositories.class */
public class Repositories implements Iterable<Class<?>> {
    static final Repositories NONE = new Repositories();
    private static final RepositoryFactoryInformation<Object, Serializable> EMPTY_REPOSITORY_FACTORY_INFO = EmptyRepositoryFactoryInformation.INSTANCE;
    private static final String DOMAIN_TYPE_MUST_NOT_BE_NULL = "Domain type must not be null!";
    private final BeanFactory beanFactory;
    private final Map<Class<?>, String> repositoryBeanNames;
    private final Map<Class<?>, RepositoryFactoryInformation<Object, Serializable>> repositoryFactoryInfos;

    private Repositories() {
        this.beanFactory = null;
        this.repositoryBeanNames = Collections.emptyMap();
        this.repositoryFactoryInfos = Collections.emptyMap();
    }

    public Repositories(ListableBeanFactory factory) {
        Assert.notNull(factory, "Factory must not be null!");
        this.beanFactory = factory;
        this.repositoryFactoryInfos = new HashMap();
        this.repositoryBeanNames = new HashMap();
        populateRepositoryFactoryInformation(factory);
    }

    private void populateRepositoryFactoryInformation(ListableBeanFactory factory) {
        for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(factory, RepositoryFactoryInformation.class, false, false)) {
            cacheRepositoryFactory(name);
        }
    }

    private synchronized void cacheRepositoryFactory(String name) {
        RepositoryFactoryInformation repositoryFactoryInformation = (RepositoryFactoryInformation) this.beanFactory.getBean(name, RepositoryFactoryInformation.class);
        Class<?> domainType = ClassUtils.getUserClass(repositoryFactoryInformation.getRepositoryInformation().getDomainType());
        RepositoryInformation information = repositoryFactoryInformation.getRepositoryInformation();
        Set<Class<?>> alternativeDomainTypes = information.getAlternativeDomainTypes();
        String beanName = BeanFactoryUtils.transformedBeanName(name);
        Set<Class<?>> typesToRegister = new HashSet<>(alternativeDomainTypes.size() + 1);
        typesToRegister.add(domainType);
        typesToRegister.addAll(alternativeDomainTypes);
        for (Class<?> type : typesToRegister) {
            this.repositoryFactoryInfos.put(type, repositoryFactoryInformation);
            this.repositoryBeanNames.put(type, beanName);
        }
    }

    public boolean hasRepositoryFor(Class<?> domainClass) {
        Assert.notNull(domainClass, DOMAIN_TYPE_MUST_NOT_BE_NULL);
        Class<?> userClass = ProxyUtils.getUserClass(domainClass);
        return this.repositoryFactoryInfos.containsKey(userClass);
    }

    public Object getRepositoryFor(Class<?> domainClass) {
        Assert.notNull(domainClass, DOMAIN_TYPE_MUST_NOT_BE_NULL);
        Class<?> userClass = ProxyUtils.getUserClass(domainClass);
        String repositoryBeanName = this.repositoryBeanNames.get(userClass);
        if (repositoryBeanName == null || this.beanFactory == null) {
            return null;
        }
        return this.beanFactory.getBean(repositoryBeanName);
    }

    private RepositoryFactoryInformation<Object, Serializable> getRepositoryFactoryInfoFor(Class<?> domainClass) {
        Assert.notNull(domainClass, DOMAIN_TYPE_MUST_NOT_BE_NULL);
        Class<?> userType = ProxyUtils.getUserClass(domainClass);
        RepositoryFactoryInformation<Object, Serializable> repositoryInfo = this.repositoryFactoryInfos.get(userType);
        if (repositoryInfo != null) {
            return repositoryInfo;
        }
        if (!userType.equals(Object.class)) {
            return getRepositoryFactoryInfoFor(userType.getSuperclass());
        }
        return EMPTY_REPOSITORY_FACTORY_INFO;
    }

    public <T, S extends Serializable> EntityInformation<T, S> getEntityInformationFor(Class<?> cls) {
        Assert.notNull(cls, DOMAIN_TYPE_MUST_NOT_BE_NULL);
        return (EntityInformation<T, S>) getRepositoryFactoryInfoFor(cls).getEntityInformation();
    }

    public RepositoryInformation getRepositoryInformationFor(Class<?> domainClass) {
        Assert.notNull(domainClass, DOMAIN_TYPE_MUST_NOT_BE_NULL);
        RepositoryFactoryInformation<Object, Serializable> information = getRepositoryFactoryInfoFor(domainClass);
        if (information == EMPTY_REPOSITORY_FACTORY_INFO) {
            return null;
        }
        return information.getRepositoryInformation();
    }

    public RepositoryInformation getRepositoryInformation(Class<?> repositoryInterface) {
        for (RepositoryFactoryInformation<Object, Serializable> factoryInformation : this.repositoryFactoryInfos.values()) {
            RepositoryInformation information = factoryInformation.getRepositoryInformation();
            if (information.getRepositoryInterface().equals(repositoryInterface)) {
                return information;
            }
        }
        return null;
    }

    public PersistentEntity<?, ?> getPersistentEntity(Class<?> domainClass) {
        Assert.notNull(domainClass, DOMAIN_TYPE_MUST_NOT_BE_NULL);
        return getRepositoryFactoryInfoFor(domainClass).getPersistentEntity();
    }

    public List<QueryMethod> getQueryMethodsFor(Class<?> domainClass) {
        Assert.notNull(domainClass, DOMAIN_TYPE_MUST_NOT_BE_NULL);
        return getRepositoryFactoryInfoFor(domainClass).getQueryMethods();
    }

    @Override // java.lang.Iterable
    public Iterator<Class<?>> iterator() {
        return this.repositoryFactoryInfos.keySet().iterator();
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/Repositories$EmptyRepositoryFactoryInformation.class */
    private enum EmptyRepositoryFactoryInformation implements RepositoryFactoryInformation<Object, Serializable> {
        INSTANCE;

        @Override // org.springframework.data.repository.core.support.RepositoryFactoryInformation
        public EntityInformation<Object, Serializable> getEntityInformation() {
            return null;
        }

        @Override // org.springframework.data.repository.core.support.RepositoryFactoryInformation
        public RepositoryInformation getRepositoryInformation() {
            return null;
        }

        @Override // org.springframework.data.repository.core.support.RepositoryFactoryInformation
        public PersistentEntity<?, ?> getPersistentEntity() {
            return null;
        }

        @Override // org.springframework.data.repository.core.support.RepositoryFactoryInformation
        public List<QueryMethod> getQueryMethods() {
            return Collections.emptyList();
        }
    }
}
