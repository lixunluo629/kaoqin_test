package org.springframework.data.redis.support.collections;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisOperations;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/RedisProperties.class */
public class RedisProperties extends Properties implements RedisMap<Object, Object> {
    private final BoundHashOperations<String, String, String> hashOps;
    private final RedisMap<String, String> delegate;

    public RedisProperties(BoundHashOperations<String, String, String> boundOps) {
        this((Properties) null, boundOps);
    }

    public RedisProperties(String key, RedisOperations<String, ?> operations) {
        this((Properties) null, (BoundHashOperations<String, String, String>) operations.boundHashOps(key));
    }

    public RedisProperties(Properties defaults, BoundHashOperations<String, String, String> boundOps) {
        super(defaults);
        this.hashOps = boundOps;
        this.delegate = new DefaultRedisMap(boundOps);
    }

    public RedisProperties(Properties defaults, String key, RedisOperations<String, ?> operations) {
        this(defaults, (BoundHashOperations<String, String, String>) operations.boundHashOps(key));
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized Object get(Object key) {
        return this.delegate.get(key);
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized Object put(Object key, Object value) {
        return this.delegate.put((String) key, (String) value);
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized void putAll(Map<? extends Object, ? extends Object> t) {
        this.delegate.putAll(t);
    }

    @Override // java.util.Properties
    public Enumeration<?> propertyNames() {
        Set<String> keys = new LinkedHashSet<>(this.delegate.keySet());
        if (this.defaults != null) {
            keys.addAll(this.defaults.stringPropertyNames());
        }
        return Collections.enumeration(keys);
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized void clear() {
        this.delegate.clear();
    }

    @Override // java.util.Hashtable
    public synchronized Object clone() {
        return new RedisProperties(this.defaults, this.hashOps);
    }

    @Override // java.util.Hashtable
    public synchronized boolean contains(Object value) {
        return containsValue(value);
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized boolean containsKey(Object key) {
        return this.delegate.containsKey(key);
    }

    @Override // java.util.Hashtable, java.util.Map
    public boolean containsValue(Object value) {
        return this.delegate.containsValue(value);
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public synchronized Enumeration<Object> elements() {
        Collection values = this.delegate.values();
        return Collections.enumeration(values);
    }

    @Override // java.util.Hashtable, java.util.Map
    public Set<Map.Entry<Object, Object>> entrySet() {
        Set entries = this.delegate.entrySet();
        return entries;
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof RedisProperties) && o.hashCode() == hashCode();
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized int hashCode() {
        int hash = RedisProperties.class.hashCode();
        return (hash * 17) + this.delegate.hashCode();
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public synchronized Enumeration<Object> keys() {
        Set<Object> keys = keySet();
        return Collections.enumeration(keys);
    }

    @Override // java.util.Hashtable, java.util.Map
    public Set<Object> keySet() {
        Set keys = this.delegate.keySet();
        return keys;
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized Object remove(Object key) {
        return this.delegate.remove(key);
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized int size() {
        return this.delegate.size();
    }

    @Override // java.util.Hashtable, java.util.Map
    public Collection<Object> values() {
        Collection vals = this.delegate.values();
        return vals;
    }

    @Override // org.springframework.data.redis.support.collections.RedisMap
    public Long increment(Object key, long delta) {
        return this.hashOps.increment((BoundHashOperations<String, String, String>) key, delta);
    }

    @Override // org.springframework.data.redis.support.collections.RedisMap
    public Double increment(Object key, double delta) {
        return this.hashOps.increment((BoundHashOperations<String, String, String>) key, delta);
    }

    @Override // org.springframework.data.redis.support.collections.RedisStore
    public RedisOperations<String, ?> getOperations() {
        return this.hashOps.getOperations();
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

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public String getKey() {
        return this.hashOps.getKey();
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return this.hashOps.getType();
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean persist() {
        return this.hashOps.persist();
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public void rename(String newKey) {
        this.hashOps.rename(newKey);
    }

    @Override // java.util.Hashtable, java.util.Map, java.util.concurrent.ConcurrentMap
    public Object putIfAbsent(Object key, Object value) {
        if (this.hashOps.putIfAbsent((String) key, (String) value).booleanValue()) {
            return null;
        }
        return get(key);
    }

    @Override // java.util.Hashtable, java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean remove(Object key, Object value) {
        return this.delegate.remove(key, value);
    }

    @Override // java.util.Hashtable, java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean replace(Object key, Object oldValue, Object newValue) {
        return this.delegate.replace((String) key, (String) oldValue, (String) newValue);
    }

    @Override // java.util.Hashtable, java.util.Map, java.util.concurrent.ConcurrentMap
    public Object replace(Object key, Object value) {
        return this.delegate.replace((String) key, (String) value);
    }

    @Override // java.util.Properties
    public synchronized void storeToXML(OutputStream os, String comment, String encoding) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Properties
    public synchronized void storeToXML(OutputStream os, String comment) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.support.collections.RedisMap
    public Iterator<Map.Entry<Object, Object>> scan() {
        throw new UnsupportedOperationException();
    }
}
