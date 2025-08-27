package org.springframework.data.redis.core;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultBoundListOperations.class */
class DefaultBoundListOperations<K, V> extends DefaultBoundKeyOperations<K> implements BoundListOperations<K, V> {
    private final ListOperations<K, V> ops;

    public DefaultBoundListOperations(K key, RedisOperations<K, V> operations) {
        super(key, operations);
        this.ops = operations.opsForList();
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public RedisOperations<K, V> getOperations() {
        return this.ops.getOperations();
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public V index(long index) {
        return this.ops.index(getKey(), index);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public V leftPop() {
        return this.ops.leftPop(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public V leftPop(long timeout, TimeUnit unit) {
        return this.ops.leftPop(getKey(), timeout, unit);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public Long leftPush(V value) {
        return this.ops.leftPush(getKey(), value);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public Long leftPushAll(V... values) {
        return this.ops.leftPushAll((ListOperations<K, V>) getKey(), values);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public Long leftPushIfPresent(V value) {
        return this.ops.leftPushIfPresent(getKey(), value);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public Long leftPush(V pivot, V value) {
        return this.ops.leftPush(getKey(), pivot, value);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public Long size() {
        return this.ops.size(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public List<V> range(long start, long end) {
        return this.ops.range(getKey(), start, end);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public Long remove(long i, Object value) {
        return this.ops.remove(getKey(), i, value);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public V rightPop() {
        return this.ops.rightPop(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public V rightPop(long timeout, TimeUnit unit) {
        return this.ops.rightPop(getKey(), timeout, unit);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public Long rightPushIfPresent(V value) {
        return this.ops.rightPushIfPresent(getKey(), value);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public Long rightPush(V value) {
        return this.ops.rightPush(getKey(), value);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public Long rightPushAll(V... values) {
        return this.ops.rightPushAll((ListOperations<K, V>) getKey(), values);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public Long rightPush(V pivot, V value) {
        return this.ops.rightPush(getKey(), pivot, value);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public void trim(long start, long end) {
        this.ops.trim(getKey(), start, end);
    }

    @Override // org.springframework.data.redis.core.BoundListOperations
    public void set(long index, V value) {
        this.ops.set(getKey(), index, value);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return DataType.LIST;
    }
}
