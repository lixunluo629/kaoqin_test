package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultHashOperations.class */
class DefaultHashOperations<K, HK, HV> extends AbstractOperations<K, Object> implements HashOperations<K, HK, HV> {
    DefaultHashOperations(RedisTemplate<K, ?> template) {
        super(template);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.redis.core.HashOperations
    public HV get(K key, Object obj) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawHashKey = rawHashKey(obj);
        byte[] rawHashValue = (byte[]) execute(new RedisCallback<byte[]>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public byte[] doInRedis2(RedisConnection connection) {
                return connection.hGet(rawKey, rawHashKey);
            }
        }, true);
        return deserializeHashValue(rawHashValue);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.redis.core.HashOperations
    public Boolean hasKey(K key, Object obj) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawHashKey = rawHashKey(obj);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.hExists(rawKey, rawHashKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.HashOperations
    public Long increment(K key, HK hashKey, final long delta) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawHashKey = rawHashKey(hashKey);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.hIncrBy(rawKey, rawHashKey, delta);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.HashOperations
    public Double increment(K key, HK hashKey, final double delta) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawHashKey = rawHashKey(hashKey);
        return (Double) execute(new RedisCallback<Double>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Double doInRedis2(RedisConnection connection) {
                return connection.hIncrBy(rawKey, rawHashKey, delta);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.HashOperations
    public Set<HK> keys(K k) {
        final byte[] bArrRawKey = rawKey(k);
        return (Set<HK>) deserializeHashKeys((Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.hKeys(bArrRawKey);
            }
        }, true));
    }

    @Override // org.springframework.data.redis.core.HashOperations
    public Long size(K key) {
        final byte[] rawKey = rawKey(key);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.hLen(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.HashOperations
    public void putAll(K key, Map<? extends HK, ? extends HV> m) {
        if (m.isEmpty()) {
            return;
        }
        final byte[] rawKey = rawKey(key);
        final Map<byte[], byte[]> hashes = new LinkedHashMap<>(m.size());
        for (Map.Entry<? extends HK, ? extends HV> entry : m.entrySet()) {
            hashes.put(rawHashKey(entry.getKey()), rawHashValue(entry.getValue()));
        }
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.7
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) {
                connection.hMSet(rawKey, hashes);
                return null;
            }
        }, true);
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.core.HashOperations
    public List<HV> multiGet(K k, Collection<HK> collection) {
        if (collection.isEmpty()) {
            return Collections.emptyList();
        }
        final byte[] bArrRawKey = rawKey(k);
        final ?? r0 = new byte[collection.size()];
        int i = 0;
        Iterator<HK> it = collection.iterator();
        while (it.hasNext()) {
            int i2 = i;
            i++;
            r0[i2] = rawHashKey(it.next());
        }
        return (List<HV>) deserializeHashValues((List) execute(new RedisCallback<List<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public List<byte[]> doInRedis2(RedisConnection connection) {
                return connection.hMGet(bArrRawKey, r0);
            }
        }, true));
    }

    @Override // org.springframework.data.redis.core.HashOperations
    public void put(K key, HK hashKey, HV value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawHashKey = rawHashKey(hashKey);
        final byte[] rawHashValue = rawHashValue(value);
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.9
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) {
                connection.hSet(rawKey, rawHashKey, rawHashValue);
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.HashOperations
    public Boolean putIfAbsent(K key, HK hashKey, HV value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawHashKey = rawHashKey(hashKey);
        final byte[] rawHashValue = rawHashValue(value);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.hSetNX(rawKey, rawHashKey, rawHashValue);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.HashOperations
    public List<HV> values(K k) {
        final byte[] bArrRawKey = rawKey(k);
        return (List<HV>) deserializeHashValues((List) execute(new RedisCallback<List<byte[]>>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public List<byte[]> doInRedis2(RedisConnection connection) {
                return connection.hVals(bArrRawKey);
            }
        }, true));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.redis.core.HashOperations
    public Long delete(K key, Object... objArr) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawHashKeys = rawHashKeys(objArr);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.hDel(rawKey, rawHashKeys);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.HashOperations
    public Map<HK, HV> entries(K key) {
        final byte[] rawKey = rawKey(key);
        Map<byte[], byte[]> entries = (Map) execute(new RedisCallback<Map<byte[], byte[]>>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Map<byte[], byte[]> doInRedis2(RedisConnection connection) {
                return connection.hGetAll(rawKey);
            }
        }, true);
        return deserializeHashMap(entries);
    }

    /* renamed from: org.springframework.data.redis.core.DefaultHashOperations$14, reason: invalid class name */
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultHashOperations$14.class */
    class AnonymousClass14 implements RedisCallback<Cursor<Map.Entry<HK, HV>>> {
        final /* synthetic */ byte[] val$rawKey;
        final /* synthetic */ ScanOptions val$options;

        AnonymousClass14(byte[] bArr, ScanOptions scanOptions) {
            this.val$rawKey = bArr;
            this.val$options = scanOptions;
        }

        @Override // org.springframework.data.redis.core.RedisCallback
        /* renamed from: doInRedis */
        public Cursor<Map.Entry<HK, HV>> doInRedis2(RedisConnection connection) throws DataAccessException {
            return new ConvertingCursor(connection.hScan(this.val$rawKey, this.val$options), new Converter<Map.Entry<byte[], byte[]>, Map.Entry<HK, HV>>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.14.1
                @Override // org.springframework.core.convert.converter.Converter
                /* renamed from: convert, reason: avoid collision after fix types in other method */
                public Map.Entry<HK, HV> convert2(final Map.Entry<byte[], byte[]> source) {
                    return new Map.Entry<HK, HV>() { // from class: org.springframework.data.redis.core.DefaultHashOperations.14.1.1
                        @Override // java.util.Map.Entry
                        public HK getKey() {
                            return (HK) DefaultHashOperations.this.deserializeHashKey((byte[]) source.getKey());
                        }

                        @Override // java.util.Map.Entry
                        public HV getValue() {
                            return (HV) DefaultHashOperations.this.deserializeHashValue((byte[]) source.getValue());
                        }

                        @Override // java.util.Map.Entry
                        public HV setValue(HV value) {
                            throw new UnsupportedOperationException("Values cannot be set when scanning through entries.");
                        }
                    };
                }
            });
        }
    }

    @Override // org.springframework.data.redis.core.HashOperations
    public Cursor<Map.Entry<HK, HV>> scan(K key, ScanOptions options) {
        byte[] rawKey = rawKey(key);
        return (Cursor) this.template.executeWithStickyConnection(new AnonymousClass14(rawKey, options));
    }
}
