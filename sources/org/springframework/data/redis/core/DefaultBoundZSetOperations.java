package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.Set;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.ZSetOperations;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultBoundZSetOperations.class */
class DefaultBoundZSetOperations<K, V> extends DefaultBoundKeyOperations<K> implements BoundZSetOperations<K, V> {
    private final ZSetOperations<K, V> ops;

    public DefaultBoundZSetOperations(K key, RedisOperations<K, V> operations) {
        super(key, operations);
        this.ops = operations.opsForZSet();
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Boolean add(V value, double score) {
        return this.ops.add(getKey(), value, score);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Long add(Set<ZSetOperations.TypedTuple<V>> tuples) {
        return this.ops.add(getKey(), tuples);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Double incrementScore(V value, double delta) {
        return this.ops.incrementScore(getKey(), value, delta);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public RedisOperations<K, V> getOperations() {
        return this.ops.getOperations();
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public void intersectAndStore(K otherKey, K destKey) {
        this.ops.intersectAndStore(getKey(), otherKey, destKey);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public void intersectAndStore(Collection<K> otherKeys, K destKey) {
        this.ops.intersectAndStore((Collection<K>) getKey(), (Collection<Collection<K>>) otherKeys, (Collection<K>) destKey);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Set<V> range(long start, long end) {
        return this.ops.range(getKey(), start, end);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Set<V> rangeByScore(double min, double max) {
        return this.ops.rangeByScore(getKey(), min, max);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(double min, double max) {
        return this.ops.rangeByScoreWithScores(getKey(), min, max);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Set<ZSetOperations.TypedTuple<V>> rangeWithScores(long start, long end) {
        return this.ops.rangeWithScores(getKey(), start, end);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Set<V> reverseRangeByScore(double min, double max) {
        return this.ops.reverseRangeByScore(getKey(), min, max);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(double min, double max) {
        return this.ops.reverseRangeByScoreWithScores(getKey(), min, max);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Set<ZSetOperations.TypedTuple<V>> reverseRangeWithScores(long start, long end) {
        return this.ops.reverseRangeWithScores(getKey(), start, end);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Set<V> rangeByLex(RedisZSetCommands.Range range) {
        return rangeByLex(range, null);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Set<V> rangeByLex(RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return this.ops.rangeByLex(getKey(), range, limit);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Long rank(Object o) {
        return this.ops.rank(getKey(), o);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Long reverseRank(Object o) {
        return this.ops.reverseRank(getKey(), o);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Double score(Object o) {
        return this.ops.score(getKey(), o);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Long remove(Object... values) {
        return this.ops.remove(getKey(), values);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public void removeRange(long start, long end) {
        this.ops.removeRange(getKey(), start, end);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public void removeRangeByScore(double min, double max) {
        this.ops.removeRangeByScore(getKey(), min, max);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Set<V> reverseRange(long start, long end) {
        return this.ops.reverseRange(getKey(), start, end);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Long count(double min, double max) {
        return this.ops.count(getKey(), min, max);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Long size() {
        return zCard();
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Long zCard() {
        return this.ops.zCard(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public void unionAndStore(K otherKey, K destKey) {
        this.ops.unionAndStore(getKey(), otherKey, destKey);
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public void unionAndStore(Collection<K> otherKeys, K destKey) {
        this.ops.unionAndStore((Collection<K>) getKey(), (Collection<Collection<K>>) otherKeys, (Collection<K>) destKey);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return DataType.ZSET;
    }

    @Override // org.springframework.data.redis.core.BoundZSetOperations
    public Cursor<ZSetOperations.TypedTuple<V>> scan(ScanOptions options) {
        return this.ops.scan(getKey(), options);
    }
}
