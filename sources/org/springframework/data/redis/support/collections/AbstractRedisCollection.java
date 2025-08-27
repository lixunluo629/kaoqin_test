package org.springframework.data.redis.support.collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisOperations;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/AbstractRedisCollection.class */
public abstract class AbstractRedisCollection<E> extends AbstractCollection<E> implements RedisCollection<E> {
    public static final String ENCODING = "UTF-8";
    private volatile String key;
    private final RedisOperations<String, E> operations;

    public <K> AbstractRedisCollection(String key, RedisOperations<String, E> operations) {
        this.key = key;
        this.operations = operations;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public String getKey() {
        return this.key;
    }

    @Override // org.springframework.data.redis.support.collections.RedisStore
    public RedisOperations<String, E> getOperations() {
        return this.operations;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            modified |= add(e);
        }
        return modified;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        boolean contains = true;
        for (Object object : c) {
            contains &= contains(object);
        }
        return contains;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object object : c) {
            modified |= remove(object);
        }
        return modified;
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean expire(long timeout, TimeUnit unit) {
        return this.operations.expire(this.key, timeout, unit);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean expireAt(Date date) {
        return this.operations.expireAt(this.key, date);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Long getExpire() {
        return this.operations.getExpire(this.key);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public Boolean persist() {
        return this.operations.persist(this.key);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public void rename(String newKey) {
        if (!isEmpty()) {
            CollectionUtils.rename(this.key, newKey, this.operations);
        }
        this.key = newKey;
    }

    protected void checkResult(Object obj) {
        if (obj == null) {
            throw new IllegalStateException("Cannot read collection with Redis connection in pipeline/multi-exec mode");
        }
    }

    @Override // java.util.Collection
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof RedisStore) {
            return this.key.equals(((RedisStore) o).getKey());
        }
        return (o instanceof AbstractRedisCollection) && o.hashCode() == hashCode();
    }

    @Override // java.util.Collection
    public int hashCode() {
        int result = 17 + getClass().hashCode();
        return (result * 31) + this.key.hashCode();
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return "RedisStore for key:" + getKey();
    }
}
