package org.springframework.data.redis.connection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DecoratedRedisConnection.class */
public interface DecoratedRedisConnection {
    RedisConnection getDelegate();
}
