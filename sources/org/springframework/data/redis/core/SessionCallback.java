package org.springframework.data.redis.core;

import org.springframework.dao.DataAccessException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/SessionCallback.class */
public interface SessionCallback<T> {
    <K, V> T execute(RedisOperations<K, V> redisOperations) throws DataAccessException;
}
