package org.springframework.data.redis.hash;

import java.util.Map;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/hash/HashMapper.class */
public interface HashMapper<T, K, V> {
    Map<K, V> toHash(T t);

    T fromHash(Map<K, V> map);
}
