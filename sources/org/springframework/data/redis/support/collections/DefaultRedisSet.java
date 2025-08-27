package org.springframework.data.redis.support.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/DefaultRedisSet.class */
public class DefaultRedisSet<E> extends AbstractRedisCollection<E> implements RedisSet<E> {
    private final BoundSetOperations<String, E> boundSetOps;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/DefaultRedisSet$DefaultRedisSetIterator.class */
    private class DefaultRedisSetIterator extends RedisIterator<E> {
        public DefaultRedisSetIterator(Iterator<E> delegate) {
            super(delegate);
        }

        @Override // org.springframework.data.redis.support.collections.RedisIterator
        protected void removeFromRedisStorage(E item) {
            DefaultRedisSet.this.remove(item);
        }
    }

    public DefaultRedisSet(String key, RedisOperations<String, E> operations) {
        super(key, operations);
        this.boundSetOps = operations.boundSetOps(key);
    }

    public DefaultRedisSet(BoundSetOperations<String, E> boundOps) {
        super(boundOps.getKey(), boundOps.getOperations());
        this.boundSetOps = boundOps;
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public Set<E> diff(RedisSet<?> set) {
        return this.boundSetOps.diff((BoundSetOperations<String, E>) set.getKey());
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public Set<E> diff(Collection<? extends RedisSet<?>> sets) {
        return this.boundSetOps.diff(CollectionUtils.extractKeys(sets));
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public RedisSet<E> diffAndStore(RedisSet<?> set, String destKey) {
        this.boundSetOps.diffAndStore(set.getKey(), destKey);
        return new DefaultRedisSet(this.boundSetOps.getOperations().boundSetOps(destKey));
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public RedisSet<E> diffAndStore(Collection<? extends RedisSet<?>> sets, String destKey) {
        this.boundSetOps.diffAndStore((Collection<Collection<String>>) CollectionUtils.extractKeys(sets), (Collection<String>) destKey);
        return new DefaultRedisSet(this.boundSetOps.getOperations().boundSetOps(destKey));
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public Set<E> intersect(RedisSet<?> set) {
        return this.boundSetOps.intersect((BoundSetOperations<String, E>) set.getKey());
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public Set<E> intersect(Collection<? extends RedisSet<?>> sets) {
        return this.boundSetOps.intersect(CollectionUtils.extractKeys(sets));
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public RedisSet<E> intersectAndStore(RedisSet<?> set, String destKey) {
        this.boundSetOps.intersectAndStore(set.getKey(), destKey);
        return new DefaultRedisSet(this.boundSetOps.getOperations().boundSetOps(destKey));
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public RedisSet<E> intersectAndStore(Collection<? extends RedisSet<?>> sets, String destKey) {
        this.boundSetOps.intersectAndStore((Collection<Collection<String>>) CollectionUtils.extractKeys(sets), (Collection<String>) destKey);
        return new DefaultRedisSet(this.boundSetOps.getOperations().boundSetOps(destKey));
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public Set<E> union(RedisSet<?> set) {
        return this.boundSetOps.union((BoundSetOperations<String, E>) set.getKey());
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public Set<E> union(Collection<? extends RedisSet<?>> sets) {
        return this.boundSetOps.union(CollectionUtils.extractKeys(sets));
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public RedisSet<E> unionAndStore(RedisSet<?> set, String destKey) {
        this.boundSetOps.unionAndStore(set.getKey(), destKey);
        return new DefaultRedisSet(this.boundSetOps.getOperations().boundSetOps(destKey));
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public RedisSet<E> unionAndStore(Collection<? extends RedisSet<?>> sets, String destKey) {
        this.boundSetOps.unionAndStore((Collection<Collection<String>>) CollectionUtils.extractKeys(sets), (Collection<String>) destKey);
        return new DefaultRedisSet(this.boundSetOps.getOperations().boundSetOps(destKey));
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(E e) {
        Long lAdd = this.boundSetOps.add(e);
        checkResult(lAdd);
        return lAdd.longValue() == 1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        String randomKey = UUID.randomUUID().toString();
        this.boundSetOps.intersectAndStore((Collection<Set>) Collections.singleton(randomKey), (Set) getKey());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o) {
        Boolean result = this.boundSetOps.isMember(o);
        checkResult(result);
        return result.booleanValue();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        Set<E> members = this.boundSetOps.members();
        checkResult(members);
        return new DefaultRedisSetIterator(members.iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o) {
        Long result = this.boundSetOps.remove(o);
        checkResult(result);
        return result.longValue() == 1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        Long result = this.boundSetOps.size();
        checkResult(result);
        return result.intValue();
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return DataType.SET;
    }

    @Override // org.springframework.data.redis.support.collections.RedisSet
    public Cursor<E> scan() {
        return scan(ScanOptions.NONE);
    }

    public Cursor<E> scan(ScanOptions options) {
        return this.boundSetOps.scan(options);
    }
}
