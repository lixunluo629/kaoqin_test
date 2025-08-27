package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.redis.connection.DataType;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultBoundHashOperations.class */
class DefaultBoundHashOperations<H, HK, HV> extends DefaultBoundKeyOperations<H> implements BoundHashOperations<H, HK, HV> {
    private final HashOperations<H, HK, HV> ops;

    public DefaultBoundHashOperations(H key, RedisOperations<H, ?> operations) {
        super(key, operations);
        this.ops = operations.opsForHash();
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public Long delete(Object... keys) {
        return this.ops.delete(getKey(), keys);
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public HV get(Object key) {
        return this.ops.get(getKey(), key);
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public List<HV> multiGet(Collection<HK> hashKeys) {
        return this.ops.multiGet(getKey(), hashKeys);
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public RedisOperations<H, ?> getOperations() {
        return this.ops.getOperations();
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public Boolean hasKey(Object key) {
        return this.ops.hasKey(getKey(), key);
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public Long increment(HK key, long delta) {
        return this.ops.increment((HashOperations<H, HK, HV>) getKey(), (H) key, delta);
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public Double increment(HK key, double delta) {
        return this.ops.increment((HashOperations<H, HK, HV>) getKey(), (H) key, delta);
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public Set<HK> keys() {
        return this.ops.keys(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public Long size() {
        return this.ops.size(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public void putAll(Map<? extends HK, ? extends HV> m) {
        this.ops.putAll(getKey(), m);
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public void put(HK key, HV value) {
        this.ops.put(getKey(), key, value);
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public Boolean putIfAbsent(HK key, HV value) {
        return this.ops.putIfAbsent(getKey(), key, value);
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public List<HV> values() {
        return this.ops.values(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public Map<HK, HV> entries() {
        return this.ops.entries(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return DataType.HASH;
    }

    @Override // org.springframework.data.redis.core.BoundHashOperations
    public Cursor<Map.Entry<HK, HV>> scan(ScanOptions options) {
        return this.ops.scan(getKey(), options);
    }
}
