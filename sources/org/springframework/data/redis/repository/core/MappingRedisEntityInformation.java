package org.springframework.data.redis.repository.core;

import java.io.Serializable;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.redis.core.mapping.RedisPersistentEntity;
import org.springframework.data.repository.core.support.PersistentEntityInformation;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/core/MappingRedisEntityInformation.class */
public class MappingRedisEntityInformation<T, ID extends Serializable> extends PersistentEntityInformation<T, Serializable> implements RedisEntityInformation<T, Serializable> {
    private final RedisPersistentEntity<T> entityMetadata;

    public MappingRedisEntityInformation(RedisPersistentEntity<T> entity) {
        super(entity);
        this.entityMetadata = entity;
        if (!this.entityMetadata.hasIdProperty()) {
            throw new MappingException(String.format("Entity %s requires to have an explicit id field. Did you forget to provide one using @Id?", entity.getName()));
        }
    }
}
