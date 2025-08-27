package org.springframework.data.redis.support.collections;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/RedisMap.class */
public interface RedisMap<K, V> extends RedisStore, ConcurrentMap<K, V> {
    Long increment(K k, long j);

    Double increment(K k, double d);

    Iterator<Map.Entry<K, V>> scan();
}
