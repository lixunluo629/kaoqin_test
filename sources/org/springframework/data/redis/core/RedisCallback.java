package org.springframework.data.redis.core;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisCallback.class */
public interface RedisCallback<T> {
    T doInRedis(RedisConnection redisConnection) throws DataAccessException;
}
