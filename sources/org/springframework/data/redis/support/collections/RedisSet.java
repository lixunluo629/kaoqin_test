package org.springframework.data.redis.support.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/RedisSet.class */
public interface RedisSet<E> extends RedisCollection<E>, Set<E> {
    Set<E> intersect(RedisSet<?> redisSet);

    Set<E> intersect(Collection<? extends RedisSet<?>> collection);

    Set<E> union(RedisSet<?> redisSet);

    Set<E> union(Collection<? extends RedisSet<?>> collection);

    Set<E> diff(RedisSet<?> redisSet);

    Set<E> diff(Collection<? extends RedisSet<?>> collection);

    RedisSet<E> intersectAndStore(RedisSet<?> redisSet, String str);

    RedisSet<E> intersectAndStore(Collection<? extends RedisSet<?>> collection, String str);

    RedisSet<E> unionAndStore(RedisSet<?> redisSet, String str);

    RedisSet<E> unionAndStore(Collection<? extends RedisSet<?>> collection, String str);

    RedisSet<E> diffAndStore(RedisSet<?> redisSet, String str);

    RedisSet<E> diffAndStore(Collection<? extends RedisSet<?>> collection, String str);

    Iterator<E> scan();
}
