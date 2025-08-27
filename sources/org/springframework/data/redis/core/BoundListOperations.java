package org.springframework.data.redis.core;

import java.util.List;
import java.util.concurrent.TimeUnit;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/BoundListOperations.class */
public interface BoundListOperations<K, V> extends BoundKeyOperations<K> {
    List<V> range(long j, long j2);

    void trim(long j, long j2);

    Long size();

    Long leftPush(V v);

    Long leftPushAll(V... vArr);

    Long leftPushIfPresent(V v);

    Long leftPush(V v, V v2);

    Long rightPush(V v);

    Long rightPushAll(V... vArr);

    Long rightPushIfPresent(V v);

    Long rightPush(V v, V v2);

    void set(long j, V v);

    Long remove(long j, Object obj);

    V index(long j);

    V leftPop();

    V leftPop(long j, TimeUnit timeUnit);

    V rightPop();

    V rightPop(long j, TimeUnit timeUnit);

    RedisOperations<K, V> getOperations();
}
