package org.springframework.data.redis.connection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/Pool.class */
public interface Pool<T> {
    T getResource();

    void returnBrokenResource(T t);

    void returnResource(T t);

    void destroy();
}
