package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.Set;
import org.springframework.data.redis.connection.RedisZSetCommands;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ZSetOperations.class */
public interface ZSetOperations<K, V> {

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ZSetOperations$TypedTuple.class */
    public interface TypedTuple<V> extends Comparable<TypedTuple<V>> {
        V getValue();

        Double getScore();
    }

    Boolean add(K k, V v, double d);

    Long add(K k, Set<TypedTuple<V>> set);

    Long remove(K k, Object... objArr);

    Double incrementScore(K k, V v, double d);

    Long rank(K k, Object obj);

    Long reverseRank(K k, Object obj);

    Set<V> range(K k, long j, long j2);

    Set<TypedTuple<V>> rangeWithScores(K k, long j, long j2);

    Set<V> rangeByScore(K k, double d, double d2);

    Set<TypedTuple<V>> rangeByScoreWithScores(K k, double d, double d2);

    Set<V> rangeByScore(K k, double d, double d2, long j, long j2);

    Set<TypedTuple<V>> rangeByScoreWithScores(K k, double d, double d2, long j, long j2);

    Set<V> reverseRange(K k, long j, long j2);

    Set<TypedTuple<V>> reverseRangeWithScores(K k, long j, long j2);

    Set<V> reverseRangeByScore(K k, double d, double d2);

    Set<TypedTuple<V>> reverseRangeByScoreWithScores(K k, double d, double d2);

    Set<V> reverseRangeByScore(K k, double d, double d2, long j, long j2);

    Set<TypedTuple<V>> reverseRangeByScoreWithScores(K k, double d, double d2, long j, long j2);

    Long count(K k, double d, double d2);

    Long size(K k);

    Long zCard(K k);

    Double score(K k, Object obj);

    Long removeRange(K k, long j, long j2);

    Long removeRangeByScore(K k, double d, double d2);

    Long unionAndStore(K k, K k2, K k3);

    Long unionAndStore(K k, Collection<K> collection, K k2);

    Long intersectAndStore(K k, K k2, K k3);

    Long intersectAndStore(K k, Collection<K> collection, K k2);

    Cursor<TypedTuple<V>> scan(K k, ScanOptions scanOptions);

    Set<V> rangeByLex(K k, RedisZSetCommands.Range range);

    Set<V> rangeByLex(K k, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit);

    RedisOperations<K, V> getOperations();
}
