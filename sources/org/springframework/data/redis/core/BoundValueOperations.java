package org.springframework.data.redis.core;

import java.util.concurrent.TimeUnit;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/BoundValueOperations.class */
public interface BoundValueOperations<K, V> extends BoundKeyOperations<K> {
    void set(V v);

    void set(V v, long j, TimeUnit timeUnit);

    Boolean setIfAbsent(V v);

    V get();

    V getAndSet(V v);

    Long increment(long j);

    Double increment(double d);

    Integer append(String str);

    String get(long j, long j2);

    void set(V v, long j);

    Long size();

    RedisOperations<K, V> getOperations();
}
