package org.springframework.data.redis.core;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultBoundKeyOperations.class */
abstract class DefaultBoundKeyOperations<K> implements BoundKeyOperations<K> {
    private K key;
    private final RedisOperations<K, ?> ops;

    public DefaultBoundKeyOperations(K key, RedisOperations<K, ?> operations) {
        setKey(key);
        this.ops = operations;
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public K getKey() {
        return this.key;
    }

    protected void setKey(K key) {
        this.key = key;
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean expire(long timeout, TimeUnit unit) {
        return this.ops.expire(this.key, timeout, unit);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean expireAt(Date date) {
        return this.ops.expireAt(this.key, date);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Long getExpire() {
        return this.ops.getExpire(this.key);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean persist() {
        return this.ops.persist(this.key);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public void rename(K newKey) {
        if (this.ops.hasKey(this.key).booleanValue()) {
            this.ops.rename(this.key, newKey);
        }
        this.key = newKey;
    }
}
