package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ListOperations.class */
public interface ListOperations<K, V> {
    List<V> range(K k, long j, long j2);

    void trim(K k, long j, long j2);

    Long size(K k);

    Long leftPush(K k, V v);

    Long leftPushAll(K k, V... vArr);

    Long leftPushAll(K k, Collection<V> collection);

    Long leftPushIfPresent(K k, V v);

    Long leftPush(K k, V v, V v2);

    Long rightPush(K k, V v);

    Long rightPushAll(K k, V... vArr);

    Long rightPushAll(K k, Collection<V> collection);

    Long rightPushIfPresent(K k, V v);

    Long rightPush(K k, V v, V v2);

    void set(K k, long j, V v);

    Long remove(K k, long j, Object obj);

    V index(K k, long j);

    V leftPop(K k);

    V leftPop(K k, long j, TimeUnit timeUnit);

    V rightPop(K k);

    V rightPop(K k, long j, TimeUnit timeUnit);

    V rightPopAndLeftPush(K k, K k2);

    V rightPopAndLeftPush(K k, K k2, long j, TimeUnit timeUnit);

    RedisOperations<K, V> getOperations();
}
