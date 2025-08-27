package org.springframework.data.redis.core.mapping;

import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.redis.core.TimeToLiveAccessor;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/mapping/RedisPersistentEntity.class */
public interface RedisPersistentEntity<T> extends KeyValuePersistentEntity<T> {
    TimeToLiveAccessor getTimeToLiveAccessor();

    boolean hasExplictTimeToLiveProperty();

    PersistentProperty<? extends PersistentProperty<?>> getExplicitTimeToLiveProperty();
}
