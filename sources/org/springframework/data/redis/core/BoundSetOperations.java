package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/BoundSetOperations.class */
public interface BoundSetOperations<K, V> extends BoundKeyOperations<K> {
    Long add(V... vArr);

    Long remove(Object... objArr);

    V pop();

    Boolean move(K k, V v);

    Long size();

    Boolean isMember(Object obj);

    Set<V> intersect(K k);

    Set<V> intersect(Collection<K> collection);

    void intersectAndStore(K k, K k2);

    void intersectAndStore(Collection<K> collection, K k);

    Set<V> union(K k);

    Set<V> union(Collection<K> collection);

    void unionAndStore(K k, K k2);

    void unionAndStore(Collection<K> collection, K k);

    Set<V> diff(K k);

    Set<V> diff(Collection<K> collection);

    void diffAndStore(K k, K k2);

    void diffAndStore(Collection<K> collection, K k);

    Set<V> members();

    V randomMember();

    Set<V> distinctRandomMembers(long j);

    List<V> randomMembers(long j);

    Cursor<V> scan(ScanOptions scanOptions);

    RedisOperations<K, V> getOperations();
}
