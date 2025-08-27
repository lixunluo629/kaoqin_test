package org.springframework.data.redis.repository.support;

import java.io.Serializable;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.query.KeyValuePartTreeQuery;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactory;
import org.springframework.data.redis.core.mapping.RedisPersistentEntity;
import org.springframework.data.redis.repository.core.MappingRedisEntityInformation;
import org.springframework.data.redis.repository.query.RedisQueryCreator;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/support/RedisRepositoryFactory.class */
public class RedisRepositoryFactory extends KeyValueRepositoryFactory {
    private final KeyValueOperations operations;

    public RedisRepositoryFactory(KeyValueOperations keyValueOperations) {
        this(keyValueOperations, RedisQueryCreator.class);
    }

    public RedisRepositoryFactory(KeyValueOperations keyValueOperations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator) {
        this(keyValueOperations, queryCreator, KeyValuePartTreeQuery.class);
    }

    public RedisRepositoryFactory(KeyValueOperations keyValueOperations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator, Class<? extends RepositoryQuery> repositoryQueryType) {
        super(keyValueOperations, queryCreator, repositoryQueryType);
        this.operations = keyValueOperations;
    }

    @Override // org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactory, org.springframework.data.repository.core.support.RepositoryFactorySupport
    public <T, ID extends Serializable> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        RedisPersistentEntity<T> entity = (RedisPersistentEntity) this.operations.getMappingContext().getPersistentEntity((Class<?>) domainClass);
        EntityInformation<T, ID> entityInformation = new MappingRedisEntityInformation<>(entity);
        return entityInformation;
    }
}
