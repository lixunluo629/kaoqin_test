package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ValueOperations.class */
public interface ValueOperations<K, V> {
    void set(K k, V v);

    void set(K k, V v, long j, TimeUnit timeUnit);

    Boolean setIfAbsent(K k, V v);

    void multiSet(Map<? extends K, ? extends V> map);

    Boolean multiSetIfAbsent(Map<? extends K, ? extends V> map);

    V get(Object obj);

    V getAndSet(K k, V v);

    List<V> multiGet(Collection<K> collection);

    Long increment(K k, long j);

    Double increment(K k, double d);

    Integer append(K k, String str);

    String get(K k, long j, long j2);

    void set(K k, V v, long j);

    Long size(K k);

    Boolean setBit(K k, long j, boolean z);

    Boolean getBit(K k, long j);

    RedisOperations<K, V> getOperations();
}
