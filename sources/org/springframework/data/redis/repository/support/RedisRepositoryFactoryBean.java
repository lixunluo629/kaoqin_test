package org.springframework.data.redis.repository.support;

import java.io.Serializable;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactory;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/support/RedisRepositoryFactoryBean.class */
public class RedisRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends KeyValueRepositoryFactoryBean<T, S, ID> {
    @Override // org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactoryBean
    protected /* bridge */ /* synthetic */ KeyValueRepositoryFactory createRepositoryFactory(KeyValueOperations keyValueOperations, Class cls, Class cls2) {
        return createRepositoryFactory(keyValueOperations, (Class<? extends AbstractQueryCreator<?, ?>>) cls, (Class<? extends RepositoryQuery>) cls2);
    }

    public RedisRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override // org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactoryBean
    protected RedisRepositoryFactory createRepositoryFactory(KeyValueOperations operations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator, Class<? extends RepositoryQuery> repositoryQueryType) {
        return new RedisRepositoryFactory(operations, queryCreator, repositoryQueryType);
    }
}
