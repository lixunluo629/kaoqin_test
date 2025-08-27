package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/SetOperations.class */
public interface SetOperations<K, V> {
    Long add(K k, V... vArr);

    Long remove(K k, Object... objArr);

    V pop(K k);

    Boolean move(K k, V v, K k2);

    Long size(K k);

    Boolean isMember(K k, Object obj);

    Set<V> intersect(K k, K k2);

    Set<V> intersect(K k, Collection<K> collection);

    Long intersectAndStore(K k, K k2, K k3);

    Long intersectAndStore(K k, Collection<K> collection, K k2);

    Set<V> union(K k, K k2);

    Set<V> union(K k, Collection<K> collection);

    Long unionAndStore(K k, K k2, K k3);

    Long unionAndStore(K k, Collection<K> collection, K k2);

    Set<V> difference(K k, K k2);

    Set<V> difference(K k, Collection<K> collection);

    Long differenceAndStore(K k, K k2, K k3);

    Long differenceAndStore(K k, Collection<K> collection, K k2);

    Set<V> members(K k);

    V randomMember(K k);

    Set<V> distinctRandomMembers(K k, long j);

    List<V> randomMembers(K k, long j);

    Cursor<V> scan(K k, ScanOptions scanOptions);

    RedisOperations<K, V> getOperations();
}
