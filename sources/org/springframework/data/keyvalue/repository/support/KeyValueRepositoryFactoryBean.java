package org.springframework.data.keyvalue.repository.support;

import java.io.Serializable;
import org.springframework.beans.BeansException;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.util.Assert;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/repository/support/KeyValueRepositoryFactoryBean.class */
public class KeyValueRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends RepositoryFactoryBeanSupport<T, S, ID> {
    private KeyValueOperations operations;
    private Class<? extends AbstractQueryCreator<?, ?>> queryCreator;
    private Class<? extends RepositoryQuery> repositoryQueryType;

    public KeyValueRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    public void setKeyValueOperations(KeyValueOperations operations) {
        Assert.notNull(operations, "KeyValueOperations must not be null!");
        this.operations = operations;
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport
    public void setMappingContext(MappingContext<?, ?> mappingContext) {
        super.setMappingContext(mappingContext);
    }

    public void setQueryCreator(Class<? extends AbstractQueryCreator<?, ?>> queryCreator) {
        Assert.notNull(queryCreator, "Query creator type must not be null!");
        this.queryCreator = queryCreator;
    }

    public void setQueryType(Class<? extends RepositoryQuery> repositoryQueryType) {
        Assert.notNull(this.queryCreator, "Query creator type must not be null!");
        this.repositoryQueryType = repositoryQueryType;
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport
    protected final RepositoryFactorySupport createRepositoryFactory() {
        return createRepositoryFactory(this.operations, this.queryCreator, this.repositoryQueryType);
    }

    protected KeyValueRepositoryFactory createRepositoryFactory(KeyValueOperations operations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator, Class<? extends RepositoryQuery> repositoryQueryType) {
        return new KeyValueRepositoryFactory(operations, queryCreator, repositoryQueryType);
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws BeansException {
        Assert.notNull(this.operations, "KeyValueOperations must not be null!");
        Assert.notNull(this.queryCreator, "Query creator type must not be null!");
        Assert.notNull(this.repositoryQueryType, "RepositoryQueryType type type must not be null!");
        super.afterPropertiesSet();
    }
}
