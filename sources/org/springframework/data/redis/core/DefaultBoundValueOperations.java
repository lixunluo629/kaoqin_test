package org.springframework.data.redis.core;

import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultBoundValueOperations.class */
class DefaultBoundValueOperations<K, V> extends DefaultBoundKeyOperations<K> implements BoundValueOperations<K, V> {
    private final ValueOperations<K, V> ops;

    public DefaultBoundValueOperations(K key, RedisOperations<K, V> operations) {
        super(key, operations);
        this.ops = operations.opsForValue();
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public V get() {
        return this.ops.get(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public V getAndSet(V value) {
        return this.ops.getAndSet(getKey(), value);
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public Long increment(long delta) {
        return this.ops.increment((ValueOperations<K, V>) getKey(), delta);
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public Double increment(double delta) {
        return this.ops.increment((ValueOperations<K, V>) getKey(), delta);
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public Integer append(String value) {
        return this.ops.append(getKey(), value);
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public String get(long start, long end) {
        return this.ops.get(getKey(), start, end);
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public void set(V value, long timeout, TimeUnit unit) {
        this.ops.set(getKey(), value, timeout, unit);
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public void set(V value) {
        this.ops.set(getKey(), value);
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public Boolean setIfAbsent(V value) {
        return this.ops.setIfAbsent(getKey(), value);
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public void set(V value, long offset) {
        this.ops.set(getKey(), value, offset);
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public Long size() {
        return this.ops.size(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundValueOperations
    public RedisOperations<K, V> getOperations() {
        return this.ops.getOperations();
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return DataType.STRING;
    }
}
