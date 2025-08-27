package org.springframework.data.redis.support.collections;

import java.util.Iterator;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/RedisIterator.class */
abstract class RedisIterator<E> implements Iterator<E> {
    private final Iterator<E> delegate;
    private E item;

    protected abstract void removeFromRedisStorage(E e);

    RedisIterator(Iterator<E> delegate) {
        this.delegate = delegate;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    @Override // java.util.Iterator
    public E next() {
        this.item = this.delegate.next();
        return this.item;
    }

    @Override // java.util.Iterator
    public void remove() {
        this.delegate.remove();
        removeFromRedisStorage(this.item);
        this.item = null;
    }
}
