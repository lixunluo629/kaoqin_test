package org.ehcache.core;

import java.util.Iterator;
import org.ehcache.Cache;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.StoreAccessException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/SpecIterator.class */
class SpecIterator<K, V> implements Iterator<Cache.Entry<K, V>> {
    private final Jsr107Cache<K, V> cache;
    private final Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>> iterator;
    private Cache.Entry<K, Store.ValueHolder<V>> current;

    public SpecIterator(Jsr107Cache<K, V> cache, Store<K, V> store) {
        this.cache = cache;
        this.iterator = store.iterator();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override // java.util.Iterator
    public Cache.Entry<K, V> next() {
        try {
            Cache.Entry<K, Store.ValueHolder<V>> next = this.iterator.next();
            final K nextKey = next.getKey();
            Store.ValueHolder<V> nextValueHolder = next.getValue();
            if (this.cache.getNoLoader(nextKey) == null) {
                this.current = null;
                return null;
            }
            this.current = next;
            final V nextValue = nextValueHolder.value();
            return new Cache.Entry<K, V>() { // from class: org.ehcache.core.SpecIterator.1
                @Override // org.ehcache.Cache.Entry
                public K getKey() {
                    return (K) nextKey;
                }

                @Override // org.ehcache.Cache.Entry
                public V getValue() {
                    return (V) nextValue;
                }
            };
        } catch (StoreAccessException e) {
            this.current = null;
            return null;
        }
    }

    @Override // java.util.Iterator
    public void remove() {
        if (this.current == null) {
            throw new IllegalStateException();
        }
        this.cache.remove(this.current.getKey());
        this.current = null;
    }
}
