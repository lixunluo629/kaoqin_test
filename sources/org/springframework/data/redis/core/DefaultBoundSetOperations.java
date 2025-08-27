package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.data.redis.connection.DataType;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultBoundSetOperations.class */
class DefaultBoundSetOperations<K, V> extends DefaultBoundKeyOperations<K> implements BoundSetOperations<K, V> {
    private final SetOperations<K, V> ops;

    DefaultBoundSetOperations(K key, RedisOperations<K, V> operations) {
        super(key, operations);
        this.ops = operations.opsForSet();
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Long add(V... values) {
        return this.ops.add(getKey(), values);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Set<V> diff(K key) {
        return this.ops.difference(getKey(), key);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Set<V> diff(Collection<K> keys) {
        return this.ops.difference((SetOperations<K, V>) getKey(), (Collection<SetOperations<K, V>>) keys);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public void diffAndStore(K key, K destKey) {
        this.ops.differenceAndStore(getKey(), key, destKey);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public void diffAndStore(Collection<K> keys, K destKey) {
        this.ops.differenceAndStore((Collection<K>) getKey(), (Collection<Collection<K>>) keys, (Collection<K>) destKey);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public RedisOperations<K, V> getOperations() {
        return this.ops.getOperations();
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Set<V> intersect(K key) {
        return this.ops.intersect(getKey(), key);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Set<V> intersect(Collection<K> keys) {
        return this.ops.intersect((SetOperations<K, V>) getKey(), (Collection<SetOperations<K, V>>) keys);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public void intersectAndStore(K key, K destKey) {
        this.ops.intersectAndStore(getKey(), key, destKey);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public void intersectAndStore(Collection<K> keys, K destKey) {
        this.ops.intersectAndStore((Collection<K>) getKey(), (Collection<Collection<K>>) keys, (Collection<K>) destKey);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Boolean isMember(Object o) {
        return this.ops.isMember(getKey(), o);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Set<V> members() {
        return this.ops.members(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Boolean move(K destKey, V value) {
        return this.ops.move(getKey(), value, destKey);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public V randomMember() {
        return this.ops.randomMember(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Set<V> distinctRandomMembers(long count) {
        return this.ops.distinctRandomMembers(getKey(), count);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public List<V> randomMembers(long count) {
        return this.ops.randomMembers(getKey(), count);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Long remove(Object... values) {
        return this.ops.remove(getKey(), values);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public V pop() {
        return this.ops.pop(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Long size() {
        return this.ops.size(getKey());
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Set<V> union(K key) {
        return this.ops.union(getKey(), key);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Set<V> union(Collection<K> keys) {
        return this.ops.union((SetOperations<K, V>) getKey(), (Collection<SetOperations<K, V>>) keys);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public void unionAndStore(K key, K destKey) {
        this.ops.unionAndStore(getKey(), key, destKey);
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public void unionAndStore(Collection<K> keys, K destKey) {
        this.ops.unionAndStore((Collection<K>) getKey(), (Collection<Collection<K>>) keys, (Collection<K>) destKey);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return DataType.SET;
    }

    @Override // org.springframework.data.redis.core.BoundSetOperations
    public Cursor<V> scan(ScanOptions options) {
        return this.ops.scan(getKey(), options);
    }
}
