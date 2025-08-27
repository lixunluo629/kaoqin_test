package org.springframework.data.redis.core;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisClusterCallback.class */
public interface RedisClusterCallback<T> {
    T doInRedis(RedisClusterConnection redisClusterConnection) throws DataAccessException;
}
