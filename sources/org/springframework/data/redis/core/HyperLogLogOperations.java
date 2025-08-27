package org.springframework.data.redis.core;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/HyperLogLogOperations.class */
public interface HyperLogLogOperations<K, V> {
    Long add(K k, V... vArr);

    Long size(K... kArr);

    Long union(K k, K... kArr);

    void delete(K k);
}
