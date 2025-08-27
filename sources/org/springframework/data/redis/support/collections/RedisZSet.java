package org.springframework.data.redis.support.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.ZSetOperations;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/RedisZSet.class */
public interface RedisZSet<E> extends RedisCollection<E>, Set<E> {
    RedisZSet<E> intersectAndStore(RedisZSet<?> redisZSet, String str);

    RedisZSet<E> intersectAndStore(Collection<? extends RedisZSet<?>> collection, String str);

    RedisZSet<E> unionAndStore(RedisZSet<?> redisZSet, String str);

    RedisZSet<E> unionAndStore(Collection<? extends RedisZSet<?>> collection, String str);

    Set<E> range(long j, long j2);

    Set<E> reverseRange(long j, long j2);

    Set<E> rangeByLex(RedisZSetCommands.Range range);

    Set<E> rangeByLex(RedisZSetCommands.Range range, RedisZSetCommands.Limit limit);

    Set<E> rangeByScore(double d, double d2);

    Set<E> reverseRangeByScore(double d, double d2);

    Set<ZSetOperations.TypedTuple<E>> rangeWithScores(long j, long j2);

    Set<ZSetOperations.TypedTuple<E>> reverseRangeWithScores(long j, long j2);

    Set<ZSetOperations.TypedTuple<E>> rangeByScoreWithScores(double d, double d2);

    Set<ZSetOperations.TypedTuple<E>> reverseRangeByScoreWithScores(double d, double d2);

    RedisZSet<E> remove(long j, long j2);

    RedisZSet<E> removeByScore(double d, double d2);

    boolean add(E e, double d);

    @Override // java.util.Collection, org.springframework.data.redis.support.collections.RedisZSet, java.util.Set
    boolean add(E e);

    Double score(Object obj);

    Long rank(Object obj);

    Long reverseRank(Object obj);

    Double getDefaultScore();

    E first();

    E last();

    Iterator<E> scan();
}
