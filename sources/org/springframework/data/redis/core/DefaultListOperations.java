package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultListOperations.class */
class DefaultListOperations<K, V> extends AbstractOperations<K, V> implements ListOperations<K, V> {
    DefaultListOperations(RedisTemplate<K, V> template) {
        super(template);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public V index(K k, final long j) {
        return (V) execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(k) { // from class: org.springframework.data.redis.core.DefaultListOperations.1
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                return connection.lIndex(rawKey, j);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public V leftPop(K k) {
        return (V) execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(k) { // from class: org.springframework.data.redis.core.DefaultListOperations.2
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                return connection.lPop(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public V leftPop(K k, long j, TimeUnit timeUnit) {
        final int seconds = (int) TimeoutUtils.toSeconds(j, timeUnit);
        return (V) execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(k) { // from class: org.springframework.data.redis.core.DefaultListOperations.3
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection redisConnection) {
                List<byte[]> lPop = redisConnection.bLPop(seconds, new byte[]{rawKey});
                if (CollectionUtils.isEmpty(lPop)) {
                    return null;
                }
                return lPop.get(1);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long leftPush(K key, V value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.4
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection redisConnection) {
                return redisConnection.lPush(rawKey, new byte[]{rawValue});
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long leftPushAll(K key, V... values) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawValues = rawValues(values);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.lPush(rawKey, rawValues);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long leftPushAll(K key, Collection<V> values) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawValues = rawValues(values);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.lPush(rawKey, rawValues);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long leftPushIfPresent(K key, V value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.lPushX(rawKey, rawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long leftPush(K key, V pivot, V value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawPivot = rawValue(pivot);
        final byte[] rawValue = rawValue(value);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.lInsert(rawKey, RedisListCommands.Position.BEFORE, rawPivot, rawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long size(K key) {
        final byte[] rawKey = rawKey(key);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.lLen(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public List<V> range(K k, final long j, final long j2) {
        final byte[] bArrRawKey = rawKey(k);
        return (List) execute(new RedisCallback<List<V>>() { // from class: org.springframework.data.redis.core.DefaultListOperations.10
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public List<V> doInRedis2(RedisConnection connection) {
                return DefaultListOperations.this.deserializeValues(connection.lRange(bArrRawKey, j, j2));
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long remove(K key, final long count, Object value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.lRem(rawKey, count, rawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public V rightPop(K k) {
        return (V) execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(k) { // from class: org.springframework.data.redis.core.DefaultListOperations.12
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                return connection.rPop(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public V rightPop(K k, long j, TimeUnit timeUnit) {
        final int seconds = (int) TimeoutUtils.toSeconds(j, timeUnit);
        return (V) execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(k) { // from class: org.springframework.data.redis.core.DefaultListOperations.13
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection redisConnection) {
                List<byte[]> bRPop = redisConnection.bRPop(seconds, new byte[]{rawKey});
                if (CollectionUtils.isEmpty(bRPop)) {
                    return null;
                }
                return bRPop.get(1);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long rightPush(K key, V value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.14
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection redisConnection) {
                return redisConnection.rPush(rawKey, new byte[]{rawValue});
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long rightPushAll(K key, V... values) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawValues = rawValues(values);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.rPush(rawKey, rawValues);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long rightPushAll(K key, Collection<V> values) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawValues = rawValues(values);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.rPush(rawKey, rawValues);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long rightPushIfPresent(K key, V value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.17
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.rPushX(rawKey, rawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public Long rightPush(K key, V pivot, V value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawPivot = rawValue(pivot);
        final byte[] rawValue = rawValue(value);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultListOperations.18
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.lInsert(rawKey, RedisListCommands.Position.AFTER, rawPivot, rawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public V rightPopAndLeftPush(K k, K k2) {
        final byte[] bArrRawKey = rawKey(k2);
        return (V) execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(k) { // from class: org.springframework.data.redis.core.DefaultListOperations.19
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawSourceKey, RedisConnection connection) {
                return connection.rPopLPush(rawSourceKey, bArrRawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public V rightPopAndLeftPush(K k, K k2, long j, TimeUnit timeUnit) {
        final int seconds = (int) TimeoutUtils.toSeconds(j, timeUnit);
        final byte[] bArrRawKey = rawKey(k2);
        return (V) execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(k) { // from class: org.springframework.data.redis.core.DefaultListOperations.20
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawSourceKey, RedisConnection connection) {
                return connection.bRPopLPush(seconds, rawSourceKey, bArrRawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public void set(K key, final long index, V value) {
        final byte[] rawValue = rawValue(value);
        execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(key) { // from class: org.springframework.data.redis.core.DefaultListOperations.21
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                connection.lSet(rawKey, index, rawValue);
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ListOperations
    public void trim(K key, final long start, final long end) {
        execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(key) { // from class: org.springframework.data.redis.core.DefaultListOperations.22
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                connection.lTrim(rawKey, start, end);
                return null;
            }
        }, true);
    }
}
