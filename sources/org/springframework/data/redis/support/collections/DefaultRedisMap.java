package org.springframework.data.redis.support.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SessionCallback;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/DefaultRedisMap.class */
public class DefaultRedisMap<K, V> implements RedisMap<K, V> {
    private final BoundHashOperations<String, K, V> hashOps;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/DefaultRedisMap$DefaultRedisMapEntry.class */
    private class DefaultRedisMapEntry implements Map.Entry<K, V> {
        private K key;
        private V value;

        public DefaultRedisMapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }

    public DefaultRedisMap(String str, RedisOperations<String, ?> redisOperations) {
        this.hashOps = (BoundHashOperations<String, K, V>) redisOperations.boundHashOps(str);
    }

    public DefaultRedisMap(BoundHashOperations<String, K, V> boundOps) {
        this.hashOps = boundOps;
    }

    @Override // org.springframework.data.redis.support.collections.RedisMap
    public Long increment(K key, long delta) {
        return this.hashOps.increment((BoundHashOperations<String, K, V>) key, delta);
    }

    @Override // org.springframework.data.redis.support.collections.RedisMap
    public Double increment(K key, double delta) {
        return this.hashOps.increment((BoundHashOperations<String, K, V>) key, delta);
    }

    @Override // org.springframework.data.redis.support.collections.RedisStore
    public RedisOperations<String, ?> getOperations() {
        return this.hashOps.getOperations();
    }

    @Override // java.util.Map
    public void clear() {
        getOperations().delete(Collections.singleton(getKey()));
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        Boolean result = this.hashOps.hasKey(key);
        checkResult(result);
        return result.booleanValue();
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<K> keySet = keySet();
        checkResult(keySet);
        Collection<V> multiGet = this.hashOps.multiGet(keySet);
        Iterator<K> keys = keySet.iterator();
        Iterator<V> values = multiGet.iterator();
        Set<Map.Entry<K, V>> entries = new LinkedHashSet<>();
        while (keys.hasNext()) {
            entries.add(new DefaultRedisMapEntry(keys.next(), values.next()));
        }
        return entries;
    }

    @Override // java.util.Map
    public V get(Object key) {
        return this.hashOps.get(key);
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return this.hashOps.keys();
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        V oldV = get(key);
        this.hashOps.put(key, value);
        return oldV;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        this.hashOps.putAll(m);
    }

    @Override // java.util.Map
    public V remove(Object key) {
        V v = get(key);
        this.hashOps.delete(key);
        return v;
    }

    @Override // java.util.Map
    public int size() {
        Long size = this.hashOps.size();
        checkResult(size);
        return size.intValue();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return this.hashOps.values();
    }

    @Override // java.util.Map
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof RedisMap) && o.hashCode() == hashCode();
    }

    @Override // java.util.Map
    public int hashCode() {
        int result = 17 + getClass().hashCode();
        return (result * 31) + getKey().hashCode();
    }

    public String toString() {
        return "RedisStore for key:" + getKey();
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public V putIfAbsent(K key, V value) {
        if (this.hashOps.putIfAbsent(key, value).booleanValue()) {
            return null;
        }
        return get(key);
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public boolean remove(final Object key, final Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return ((Boolean) this.hashOps.getOperations().execute(new SessionCallback<Boolean>() { // from class: org.springframework.data.redis.support.collections.DefaultRedisMap.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.SessionCallback
            public Boolean execute(RedisOperations ops) {
                do {
                    ops.watch((Collection) Collections.singleton(DefaultRedisMap.this.getKey()));
                    if (value.equals(DefaultRedisMap.this.get(key))) {
                        ops.multi();
                        DefaultRedisMap.this.remove(key);
                    } else {
                        return false;
                    }
                } while (ops.exec(ops.getHashValueSerializer()) == null);
                return true;
            }
        })).booleanValue();
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public boolean replace(final K key, final V oldValue, final V newValue) {
        if (oldValue == null || newValue == null) {
            throw new NullPointerException();
        }
        return ((Boolean) this.hashOps.getOperations().execute(new SessionCallback<Boolean>() { // from class: org.springframework.data.redis.support.collections.DefaultRedisMap.2
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.springframework.data.redis.core.SessionCallback
            public Boolean execute(RedisOperations ops) {
                do {
                    ops.watch((Collection) Collections.singleton(DefaultRedisMap.this.getKey()));
                    if (oldValue.equals(DefaultRedisMap.this.get(key))) {
                        ops.multi();
                        DefaultRedisMap.this.put(key, newValue);
                    } else {
                        return false;
                    }
                } while (ops.exec(ops.getHashValueSerializer()) == null);
                return true;
            }
        })).booleanValue();
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public V replace(final K k, final V v) {
        if (v == null) {
            throw new NullPointerException();
        }
        return (V) this.hashOps.getOperations().execute(new SessionCallback<V>() { // from class: org.springframework.data.redis.support.collections.DefaultRedisMap.3
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.springframework.data.redis.core.SessionCallback
            public V execute(RedisOperations redisOperations) {
                V v2;
                do {
                    redisOperations.watch((Collection) Collections.singleton(DefaultRedisMap.this.getKey()));
                    v2 = (V) DefaultRedisMap.this.get(k);
                    if (v2 != null) {
                        redisOperations.multi();
                        DefaultRedisMap.this.put(k, v);
                    } else {
                        return null;
                    }
                } while (redisOperations.exec(redisOperations.getHashValueSerializer()) == null);
                return v2;
            }
        });
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean expire(long timeout, TimeUnit unit) {
        return this.hashOps.expire(timeout, unit);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean expireAt(Date date) {
        return this.hashOps.expireAt(date);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Long getExpire() {
        return this.hashOps.getExpire();
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean persist() {
        return this.hashOps.persist();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public String getKey() {
        return this.hashOps.getKey();
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public void rename(String newKey) {
        this.hashOps.rename(newKey);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return this.hashOps.getType();
    }

    private void checkResult(Object obj) {
        if (obj == null) {
            throw new IllegalStateException("Cannot read collection with Redis connection in pipeline/multi-exec mode");
        }
    }

    @Override // org.springframework.data.redis.support.collections.RedisMap
    public Cursor<Map.Entry<K, V>> scan() {
        return scan(ScanOptions.NONE);
    }

    private Cursor<Map.Entry<K, V>> scan(ScanOptions options) {
        return this.hashOps.scan(options);
    }
}
