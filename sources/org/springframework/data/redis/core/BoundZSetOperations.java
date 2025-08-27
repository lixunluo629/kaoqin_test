package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.Set;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.ZSetOperations;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/BoundZSetOperations.class */
public interface BoundZSetOperations<K, V> extends BoundKeyOperations<K> {
    Boolean add(V v, double d);

    Long add(Set<ZSetOperations.TypedTuple<V>> set);

    Long remove(Object... objArr);

    Double incrementScore(V v, double d);

    Long rank(Object obj);

    Long reverseRank(Object obj);

    Set<V> range(long j, long j2);

    Set<ZSetOperations.TypedTuple<V>> rangeWithScores(long j, long j2);

    Set<V> rangeByScore(double d, double d2);

    Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(double d, double d2);

    Set<V> reverseRange(long j, long j2);

    Set<ZSetOperations.TypedTuple<V>> reverseRangeWithScores(long j, long j2);

    Set<V> reverseRangeByScore(double d, double d2);

    Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(double d, double d2);

    Long count(double d, double d2);

    Long size();

    Long zCard();

    Double score(Object obj);

    void removeRange(long j, long j2);

    void removeRangeByScore(double d, double d2);

    void unionAndStore(K k, K k2);

    void unionAndStore(Collection<K> collection, K k);

    void intersectAndStore(K k, K k2);

    void intersectAndStore(Collection<K> collection, K k);

    Cursor<ZSetOperations.TypedTuple<V>> scan(ScanOptions scanOptions);

    Set<V> rangeByLex(RedisZSetCommands.Range range);

    Set<V> rangeByLex(RedisZSetCommands.Range range, RedisZSetCommands.Limit limit);

    RedisOperations<K, V> getOperations();
}
