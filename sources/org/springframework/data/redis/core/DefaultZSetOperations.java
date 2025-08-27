package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.ZSetOperations;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultZSetOperations.class */
class DefaultZSetOperations<K, V> extends AbstractOperations<K, V> implements ZSetOperations<K, V> {
    DefaultZSetOperations(RedisTemplate<K, V> template) {
        super(template);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Boolean add(K key, V value, final double score) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.zAdd(rawKey, score, rawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long add(K key, Set<ZSetOperations.TypedTuple<V>> tuples) {
        final byte[] rawKey = rawKey(key);
        final Set<RedisZSetCommands.Tuple> rawValues = rawTupleValues(tuples);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.zAdd(rawKey, rawValues);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Double incrementScore(K key, V value, final double delta) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);
        return (Double) execute(new RedisCallback<Double>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Double doInRedis2(RedisConnection connection) {
                return connection.zIncrBy(rawKey, delta, rawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long intersectAndStore(K key, K otherKey, K destKey) {
        return intersectAndStore((Set) key, (Collection<Set>) Collections.singleton(otherKey), (Set) destKey);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {
        final byte[][] rawKeys = rawKeys((DefaultZSetOperations<K, V>) key, (Collection<DefaultZSetOperations<K, V>>) otherKeys);
        final byte[] rawDestKey = rawKey(destKey);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.zInterStore(rawDestKey, rawKeys);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<V> range(K key, final long start, final long end) {
        final byte[] rawKey = rawKey(key);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.zRange(rawKey, start, end);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<V> reverseRange(K key, final long start, final long end) {
        final byte[] rawKey = rawKey(key);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.zRevRange(rawKey, start, end);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<ZSetOperations.TypedTuple<V>> rangeWithScores(K key, final long start, final long end) {
        final byte[] rawKey = rawKey(key);
        Set<RedisZSetCommands.Tuple> rawValues = (Set) execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<RedisZSetCommands.Tuple> doInRedis2(RedisConnection connection) {
                return connection.zRangeWithScores(rawKey, start, end);
            }
        }, true);
        return deserializeTupleValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<ZSetOperations.TypedTuple<V>> reverseRangeWithScores(K key, final long start, final long end) {
        final byte[] rawKey = rawKey(key);
        Set<RedisZSetCommands.Tuple> rawValues = (Set) execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<RedisZSetCommands.Tuple> doInRedis2(RedisConnection connection) {
                return connection.zRevRangeWithScores(rawKey, start, end);
            }
        }, true);
        return deserializeTupleValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<V> rangeByLex(K key, RedisZSetCommands.Range range) {
        return rangeByLex(key, range, null);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<V> rangeByLex(K key, final RedisZSetCommands.Range range, final RedisZSetCommands.Limit limit) {
        final byte[] rawKey = rawKey(key);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.zRangeByLex(rawKey, range, limit);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<V> rangeByScore(K key, final double min, final double max) {
        final byte[] rawKey = rawKey(key);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.zRangeByScore(rawKey, min, max);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<V> rangeByScore(K key, final double min, final double max, final long offset, final long count) {
        final byte[] rawKey = rawKey(key);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.zRangeByScore(rawKey, min, max, offset, count);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<V> reverseRangeByScore(K key, final double min, final double max) {
        final byte[] rawKey = rawKey(key);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.zRevRangeByScore(rawKey, min, max);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<V> reverseRangeByScore(K key, final double min, final double max, final long offset, final long count) {
        final byte[] rawKey = rawKey(key);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.zRevRangeByScore(rawKey, min, max, offset, count);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(K key, final double min, final double max) {
        final byte[] rawKey = rawKey(key);
        Set<RedisZSetCommands.Tuple> rawValues = (Set) execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<RedisZSetCommands.Tuple> doInRedis2(RedisConnection connection) {
                return connection.zRangeByScoreWithScores(rawKey, min, max);
            }
        }, true);
        return deserializeTupleValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(K key, final double min, final double max, final long offset, final long count) {
        final byte[] rawKey = rawKey(key);
        Set<RedisZSetCommands.Tuple> rawValues = (Set) execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<RedisZSetCommands.Tuple> doInRedis2(RedisConnection connection) {
                return connection.zRangeByScoreWithScores(rawKey, min, max, offset, count);
            }
        }, true);
        return deserializeTupleValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(K key, final double min, final double max) {
        final byte[] rawKey = rawKey(key);
        Set<RedisZSetCommands.Tuple> rawValues = (Set) execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<RedisZSetCommands.Tuple> doInRedis2(RedisConnection connection) {
                return connection.zRevRangeByScoreWithScores(rawKey, min, max);
            }
        }, true);
        return deserializeTupleValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(K key, final double min, final double max, final long offset, final long count) {
        final byte[] rawKey = rawKey(key);
        Set<RedisZSetCommands.Tuple> rawValues = (Set) execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.17
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<RedisZSetCommands.Tuple> doInRedis2(RedisConnection connection) {
                return connection.zRevRangeByScoreWithScores(rawKey, min, max, offset, count);
            }
        }, true);
        return deserializeTupleValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long rank(K key, Object o) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(o);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.18
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                Long zRank = connection.zRank(rawKey, rawValue);
                if (zRank == null || zRank.longValue() < 0) {
                    return null;
                }
                return zRank;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long reverseRank(K key, Object o) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(o);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.19
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                Long zRank = connection.zRevRank(rawKey, rawValue);
                if (zRank == null || zRank.longValue() < 0) {
                    return null;
                }
                return zRank;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long remove(K key, Object... values) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawValues = rawValues(values);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.20
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.zRem(rawKey, rawValues);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long removeRange(K key, final long start, final long end) {
        final byte[] rawKey = rawKey(key);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.21
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.zRemRange(rawKey, start, end);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long removeRangeByScore(K key, final double min, final double max) {
        final byte[] rawKey = rawKey(key);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.22
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.zRemRangeByScore(rawKey, min, max);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Double score(K key, Object o) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(o);
        return (Double) execute(new RedisCallback<Double>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.23
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Double doInRedis2(RedisConnection connection) {
                return connection.zScore(rawKey, rawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long count(K key, final double min, final double max) {
        final byte[] rawKey = rawKey(key);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.24
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.zCount(rawKey, min, max);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long size(K key) {
        return zCard(key);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long zCard(K key) {
        final byte[] rawKey = rawKey(key);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.25
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.zCard(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long unionAndStore(K key, K otherKey, K destKey) {
        return unionAndStore((Set) key, (Collection<Set>) Collections.singleton(otherKey), (Set) destKey);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Long unionAndStore(K key, Collection<K> otherKeys, K destKey) {
        final byte[][] rawKeys = rawKeys((DefaultZSetOperations<K, V>) key, (Collection<DefaultZSetOperations<K, V>>) otherKeys);
        final byte[] rawDestKey = rawKey(destKey);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.26
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.zUnionStore(rawDestKey, rawKeys);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ZSetOperations
    public Cursor<ZSetOperations.TypedTuple<V>> scan(K key, final ScanOptions options) {
        final byte[] rawKey = rawKey(key);
        Cursor<RedisZSetCommands.Tuple> cursor = (Cursor) this.template.executeWithStickyConnection(new RedisCallback<Cursor<RedisZSetCommands.Tuple>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.27
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Cursor<RedisZSetCommands.Tuple> doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.zScan(rawKey, options);
            }
        });
        return new ConvertingCursor(cursor, new Converter<RedisZSetCommands.Tuple, ZSetOperations.TypedTuple<V>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.28
            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public ZSetOperations.TypedTuple<V> convert2(RedisZSetCommands.Tuple source) {
                return DefaultZSetOperations.this.deserializeTuple(source);
            }
        });
    }

    public Set<byte[]> rangeByScore(K key, final String min, final String max) {
        final byte[] rawKey = rawKey(key);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.29
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.zRangeByScore(rawKey, min, max);
            }
        }, true);
        return rawValues;
    }

    public Set<byte[]> rangeByScore(K key, final String min, final String max, final long offset, final long count) {
        final byte[] rawKey = rawKey(key);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultZSetOperations.30
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.zRangeByScore(rawKey, min, max, offset, count);
            }
        }, true);
        return rawValues;
    }
}
