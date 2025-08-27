package org.ehcache.impl.internal.store.basic;

import java.util.concurrent.TimeUnit;
import org.ehcache.core.spi.store.Store;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/basic/EmptyValueHolder.class */
public class EmptyValueHolder<V> implements Store.ValueHolder<V> {
    private static final Store.ValueHolder<Object> EMPTY = new EmptyValueHolder();

    public static <V> Store.ValueHolder<V> empty() {
        return (Store.ValueHolder<V>) EMPTY;
    }

    @Override // org.ehcache.ValueSupplier
    public V value() {
        return null;
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public long creationTime(TimeUnit unit) {
        return 0L;
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public long expirationTime(TimeUnit unit) {
        return 0L;
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public boolean isExpired(long expirationTime, TimeUnit unit) {
        return false;
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public long lastAccessTime(TimeUnit unit) {
        return 0L;
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public float hitRate(long now, TimeUnit unit) {
        return 0.0f;
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public long hits() {
        return 0L;
    }

    @Override // org.ehcache.core.spi.store.Store.ValueHolder
    public long getId() {
        return 0L;
    }
}
