package org.springframework.data.redis.support.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/DefaultRedisZSet.class */
public class DefaultRedisZSet<E> extends AbstractRedisCollection<E> implements RedisZSet<E> {
    private final BoundZSetOperations<String, E> boundZSetOps;
    private double defaultScore;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/DefaultRedisZSet$DefaultRedisSortedSetIterator.class */
    private class DefaultRedisSortedSetIterator extends RedisIterator<E> {
        public DefaultRedisSortedSetIterator(Iterator<E> delegate) {
            super(delegate);
        }

        @Override // org.springframework.data.redis.support.collections.RedisIterator
        protected void removeFromRedisStorage(E item) {
            DefaultRedisZSet.this.remove(item);
        }
    }

    public DefaultRedisZSet(String key, RedisOperations<String, E> operations) {
        this(key, operations, 1.0d);
    }

    public DefaultRedisZSet(String key, RedisOperations<String, E> operations, double defaultScore) {
        super(key, operations);
        this.defaultScore = 1.0d;
        this.boundZSetOps = operations.boundZSetOps(key);
        this.defaultScore = defaultScore;
    }

    public DefaultRedisZSet(BoundZSetOperations<String, E> boundOps) {
        this(boundOps, 1.0d);
    }

    public DefaultRedisZSet(BoundZSetOperations<String, E> boundOps, double defaultScore) {
        super(boundOps.getKey(), boundOps.getOperations());
        this.defaultScore = 1.0d;
        this.boundZSetOps = boundOps;
        this.defaultScore = defaultScore;
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public RedisZSet<E> intersectAndStore(RedisZSet<?> set, String destKey) {
        this.boundZSetOps.intersectAndStore(set.getKey(), destKey);
        return new DefaultRedisZSet(this.boundZSetOps.getOperations().boundZSetOps(destKey), getDefaultScore().doubleValue());
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public RedisZSet<E> intersectAndStore(Collection<? extends RedisZSet<?>> sets, String destKey) {
        this.boundZSetOps.intersectAndStore((Collection<Collection<String>>) CollectionUtils.extractKeys(sets), (Collection<String>) destKey);
        return new DefaultRedisZSet(this.boundZSetOps.getOperations().boundZSetOps(destKey), getDefaultScore().doubleValue());
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Set<E> range(long start, long end) {
        return this.boundZSetOps.range(start, end);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Set<E> reverseRange(long start, long end) {
        return this.boundZSetOps.reverseRange(start, end);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Set<E> rangeByLex(RedisZSetCommands.Range range) {
        return this.boundZSetOps.rangeByLex(range);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Set<E> rangeByLex(RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return this.boundZSetOps.rangeByLex(range, limit);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Set<E> rangeByScore(double min, double max) {
        return this.boundZSetOps.rangeByScore(min, max);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Set<E> reverseRangeByScore(double min, double max) {
        return this.boundZSetOps.reverseRangeByScore(min, max);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Set<ZSetOperations.TypedTuple<E>> rangeByScoreWithScores(double min, double max) {
        return this.boundZSetOps.rangeByScoreWithScores(min, max);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Set<ZSetOperations.TypedTuple<E>> rangeWithScores(long start, long end) {
        return this.boundZSetOps.rangeWithScores(start, end);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Set<ZSetOperations.TypedTuple<E>> reverseRangeByScoreWithScores(double min, double max) {
        return this.boundZSetOps.reverseRangeByScoreWithScores(min, max);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Set<ZSetOperations.TypedTuple<E>> reverseRangeWithScores(long start, long end) {
        return this.boundZSetOps.reverseRangeWithScores(start, end);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public RedisZSet<E> remove(long start, long end) {
        this.boundZSetOps.removeRange(start, end);
        return this;
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public RedisZSet<E> removeByScore(double min, double max) {
        this.boundZSetOps.removeRangeByScore(min, max);
        return this;
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public RedisZSet<E> unionAndStore(RedisZSet<?> set, String destKey) {
        this.boundZSetOps.unionAndStore(set.getKey(), destKey);
        return new DefaultRedisZSet(this.boundZSetOps.getOperations().boundZSetOps(destKey), getDefaultScore().doubleValue());
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public RedisZSet<E> unionAndStore(Collection<? extends RedisZSet<?>> sets, String destKey) {
        this.boundZSetOps.unionAndStore((Collection<Collection<String>>) CollectionUtils.extractKeys(sets), (Collection<String>) destKey);
        return new DefaultRedisZSet(this.boundZSetOps.getOperations().boundZSetOps(destKey), getDefaultScore().doubleValue());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, org.springframework.data.redis.support.collections.RedisZSet, java.util.Set
    public boolean add(E e) {
        Boolean result = Boolean.valueOf(add(e, getDefaultScore().doubleValue()));
        checkResult(result);
        return result.booleanValue();
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public boolean add(E e, double score) {
        Boolean result = this.boundZSetOps.add(e, score);
        checkResult(result);
        return result.booleanValue();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.boundZSetOps.removeRange(0L, -1L);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o) {
        return this.boundZSetOps.rank(o) != null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        Set<E> members = this.boundZSetOps.range(0L, -1L);
        checkResult(members);
        return new DefaultRedisSortedSetIterator(members.iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o) {
        Long result = this.boundZSetOps.remove(o);
        checkResult(result);
        return result.longValue() == 1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        Long result = this.boundZSetOps.size();
        checkResult(result);
        return result.intValue();
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Double getDefaultScore() {
        return Double.valueOf(this.defaultScore);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public E first() {
        Set<E> members = this.boundZSetOps.range(0L, 0L);
        checkResult(members);
        Iterator<E> iterator = members.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new NoSuchElementException();
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public E last() {
        Set<E> members = this.boundZSetOps.reverseRange(0L, 0L);
        checkResult(members);
        Iterator<E> iterator = members.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new NoSuchElementException();
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Long rank(Object o) {
        return this.boundZSetOps.rank(o);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Long reverseRank(Object o) {
        return this.boundZSetOps.reverseRank(o);
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Double score(Object o) {
        return this.boundZSetOps.score(o);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return DataType.ZSET;
    }

    @Override // org.springframework.data.redis.support.collections.RedisZSet
    public Cursor<E> scan() {
        return new ConvertingCursor(scan(ScanOptions.NONE), new Converter<ZSetOperations.TypedTuple<E>, E>() { // from class: org.springframework.data.redis.support.collections.DefaultRedisZSet.1
            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: merged with bridge method [inline-methods] */
            public E convert2(ZSetOperations.TypedTuple<E> source) {
                return source.getValue();
            }
        });
    }

    public Cursor<ZSetOperations.TypedTuple<E>> scan(ScanOptions options) {
        return this.boundZSetOps.scan(options);
    }
}
