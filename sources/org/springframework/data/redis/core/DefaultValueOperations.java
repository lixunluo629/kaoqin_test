package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultValueOperations.class */
class DefaultValueOperations<K, V> extends AbstractOperations<K, V> implements ValueOperations<K, V> {
    DefaultValueOperations(RedisTemplate<K, V> template) {
        super(template);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public V get(Object obj) {
        return (V) execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(obj) { // from class: org.springframework.data.redis.core.DefaultValueOperations.1
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                return connection.get(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public V getAndSet(K k, V v) {
        final byte[] bArrRawValue = rawValue(v);
        return (V) execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(k) { // from class: org.springframework.data.redis.core.DefaultValueOperations.2
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                return connection.getSet(rawKey, bArrRawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public Long increment(K key, final long delta) {
        final byte[] rawKey = rawKey(key);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.incrBy(rawKey, delta);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public Double increment(K key, final double delta) {
        final byte[] rawKey = rawKey(key);
        return (Double) execute(new RedisCallback<Double>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Double doInRedis2(RedisConnection connection) {
                return connection.incrBy(rawKey, delta);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public Integer append(K key, String value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawString = rawString(value);
        return (Integer) execute(new RedisCallback<Integer>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Integer doInRedis2(RedisConnection connection) {
                Long result = connection.append(rawKey, rawString);
                if (result != null) {
                    return Integer.valueOf(result.intValue());
                }
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public String get(K key, final long start, final long end) {
        final byte[] rawKey = rawKey(key);
        byte[] rawReturn = (byte[]) execute(new RedisCallback<byte[]>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public byte[] doInRedis2(RedisConnection connection) {
                return connection.getRange(rawKey, start, end);
            }
        }, true);
        return deserializeString(rawReturn);
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.core.ValueOperations
    public List<V> multiGet(Collection<K> keys) {
        if (keys.isEmpty()) {
            return Collections.emptyList();
        }
        final ?? r0 = new byte[keys.size()];
        int counter = 0;
        for (K hashKey : keys) {
            int i = counter;
            counter++;
            r0[i] = rawKey(hashKey);
        }
        List<byte[]> rawValues = (List) execute(new RedisCallback<List<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public List<byte[]> doInRedis2(RedisConnection connection) {
                return connection.mGet(r0);
            }
        }, true);
        return deserializeValues(rawValues);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public void multiSet(Map<? extends K, ? extends V> m) {
        if (m.isEmpty()) {
            return;
        }
        final Map<byte[], byte[]> rawKeys = new LinkedHashMap<>(m.size());
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            rawKeys.put(rawKey(entry.getKey()), rawValue(entry.getValue()));
        }
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.8
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) {
                connection.mSet(rawKeys);
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public Boolean multiSetIfAbsent(Map<? extends K, ? extends V> m) {
        if (m.isEmpty()) {
            return true;
        }
        final Map<byte[], byte[]> rawKeys = new LinkedHashMap<>(m.size());
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            rawKeys.put(rawKey(entry.getKey()), rawValue(entry.getValue()));
        }
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.mSetNX(rawKeys);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public void set(K key, V value) {
        final byte[] rawValue = rawValue(value);
        execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(key) { // from class: org.springframework.data.redis.core.DefaultValueOperations.10
            @Override // org.springframework.data.redis.core.AbstractOperations.ValueDeserializingRedisCallback
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                connection.set(rawKey, rawValue);
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public void set(K key, V value, final long timeout, final TimeUnit unit) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.11
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) throws DataAccessException {
                potentiallyUsePsetEx(connection);
                return null;
            }

            public void potentiallyUsePsetEx(RedisConnection connection) {
                if (!TimeUnit.MILLISECONDS.equals(unit) || !failsafeInvokePsetEx(connection)) {
                    connection.setEx(rawKey, TimeoutUtils.toSeconds(timeout, unit), rawValue);
                }
            }

            private boolean failsafeInvokePsetEx(RedisConnection connection) {
                boolean failed = false;
                try {
                    connection.pSetEx(rawKey, timeout, rawValue);
                } catch (UnsupportedOperationException e) {
                    failed = true;
                }
                return !failed;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public Boolean setIfAbsent(K key, V value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.setNX(rawKey, rawValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public void set(K key, V value, final long offset) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.13
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) {
                connection.setRange(rawKey, rawValue, offset);
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public Long size(K key) {
        final byte[] rawKey = rawKey(key);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.strLen(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public Boolean setBit(K key, final long offset, final boolean value) {
        final byte[] rawKey = rawKey(key);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.setBit(rawKey, offset, value);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.ValueOperations
    public Boolean getBit(K key, final long offset) {
        final byte[] rawKey = rawKey(key);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.DefaultValueOperations.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.getBit(rawKey, offset);
            }
        }, true);
    }
}
