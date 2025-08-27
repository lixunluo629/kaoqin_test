package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultSetOperations.class */
class DefaultSetOperations<K, V> extends AbstractOperations<K, V> implements SetOperations<K, V> {
    public DefaultSetOperations(RedisTemplate<K, V> template) {
        super(template);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Long add(K key, V... values) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawValues = rawValues(values);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.sAdd(rawKey, rawValues);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Set<V> difference(K key, K otherKey) {
        return difference((DefaultSetOperations<K, V>) key, (Collection<DefaultSetOperations<K, V>>) Collections.singleton(otherKey));
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Set<V> difference(K key, Collection<K> otherKeys) {
        final byte[][] rawKeys = rawKeys((DefaultSetOperations<K, V>) key, (Collection<DefaultSetOperations<K, V>>) otherKeys);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.sDiff(rawKeys);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Long differenceAndStore(K key, K otherKey, K destKey) {
        return differenceAndStore((Set) key, (Collection<Set>) Collections.singleton(otherKey), (Set) destKey);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Long differenceAndStore(K key, Collection<K> otherKeys, K destKey) {
        final byte[][] rawKeys = rawKeys((DefaultSetOperations<K, V>) key, (Collection<DefaultSetOperations<K, V>>) otherKeys);
        final byte[] rawDestKey = rawKey(destKey);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.sDiffStore(rawDestKey, rawKeys);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Set<V> intersect(K key, K otherKey) {
        return intersect((DefaultSetOperations<K, V>) key, (Collection<DefaultSetOperations<K, V>>) Collections.singleton(otherKey));
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Set<V> intersect(K key, Collection<K> otherKeys) {
        final byte[][] rawKeys = rawKeys((DefaultSetOperations<K, V>) key, (Collection<DefaultSetOperations<K, V>>) otherKeys);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.sInter(rawKeys);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Long intersectAndStore(K key, K otherKey, K destKey) {
        return intersectAndStore((Set) key, (Collection<Set>) Collections.singleton(otherKey), (Set) destKey);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {
        final byte[][] rawKeys = rawKeys((DefaultSetOperations<K, V>) key, (Collection<DefaultSetOperations<K, V>>) otherKeys);
        final byte[] rawDestKey = rawKey(destKey);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.sInterStore(rawDestKey, rawKeys);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Boolean isMember(K key, Object o) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(o);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.sIsMember(rawKey, rawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Set<V> members(K key) {
        final byte[] rawKey = rawKey(key);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.sMembers(rawKey);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Boolean move(K key, V value, K destKey) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawDestKey = rawKey(destKey);
        final byte[] rawValue = rawValue(value);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.sMove(rawKey, rawDestKey, rawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public V randomMember(K k) {
        return (V) execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(k) { // from class: org.springframework.data.redis.core.DefaultSetOperations.9
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                return connection.sRandMember(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Set<V> distinctRandomMembers(K key, final long count) {
        if (count < 0) {
            throw new IllegalArgumentException("Negative count not supported. Use randomMembers to allow duplicate elements.");
        }
        final byte[] rawKey = rawKey(key);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return new HashSet(connection.sRandMember(rawKey, count));
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public List<V> randomMembers(K key, final long count) {
        if (count < 0) {
            throw new IllegalArgumentException("Use a positive number for count. This method is already allowing duplicate elements.");
        }
        final byte[] rawKey = rawKey(key);
        List<byte[]> rawValues = (List) execute(new RedisCallback<List<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public List<byte[]> doInRedis2(RedisConnection connection) {
                return connection.sRandMember(rawKey, -count);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Long remove(K key, Object... values) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawValues = rawValues(values);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.sRem(rawKey, rawValues);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public V pop(K k) {
        return (V) execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(k) { // from class: org.springframework.data.redis.core.DefaultSetOperations.13
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                return connection.sPop(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Long size(K key) {
        final byte[] rawKey = rawKey(key);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.sCard(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Set<V> union(K key, K otherKey) {
        return union((DefaultSetOperations<K, V>) key, (Collection<DefaultSetOperations<K, V>>) Collections.singleton(otherKey));
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Set<V> union(K key, Collection<K> otherKeys) {
        final byte[][] rawKeys = rawKeys((DefaultSetOperations<K, V>) key, (Collection<DefaultSetOperations<K, V>>) otherKeys);
        Set<byte[]> rawValues = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.sUnion(rawKeys);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Long unionAndStore(K key, K otherKey, K destKey) {
        return unionAndStore((Set) key, (Collection<Set>) Collections.singleton(otherKey), (Set) destKey);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Long unionAndStore(K key, Collection<K> otherKeys, K destKey) {
        final byte[][] rawKeys = rawKeys((DefaultSetOperations<K, V>) key, (Collection<DefaultSetOperations<K, V>>) otherKeys);
        final byte[] rawDestKey = rawKey(destKey);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.sUnionStore(rawDestKey, rawKeys);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.SetOperations
    public Cursor<V> scan(K key, final ScanOptions options) {
        final byte[] rawKey = rawKey(key);
        return (Cursor) this.template.executeWithStickyConnection(new RedisCallback<Cursor<V>>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.17
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Cursor<V> doInRedis2(RedisConnection connection) throws DataAccessException {
                return new ConvertingCursor(connection.sScan(rawKey, options), new Converter<byte[], V>() { // from class: org.springframework.data.redis.core.DefaultSetOperations.17.1
                    @Override // org.springframework.core.convert.converter.Converter
                    /* renamed from: convert, reason: avoid collision after fix types in other method */
                    public V convert2(byte[] source) {
                        return DefaultSetOperations.this.deserializeValue(source);
                    }
                });
            }
        });
    }
}
