package org.ehcache.impl.internal.store.offheap;

import org.ehcache.core.spi.store.Store;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/BasicOffHeapValueHolder.class */
public class BasicOffHeapValueHolder<V> extends OffHeapValueHolder<V> {
    private final V value;

    public BasicOffHeapValueHolder(long id, V value, long creationTime, long expireTime) {
        this(id, value, creationTime, expireTime, 0L, 0L);
    }

    public BasicOffHeapValueHolder(long id, V value, long creationTime, long expireTime, long lastAccessTime, long hits) {
        super(id, creationTime, expireTime);
        setLastAccessTime(lastAccessTime, TIME_UNIT);
        this.value = value;
        setHits(hits);
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapValueHolder
    void updateMetadata(Store.ValueHolder<V> valueFlushed) {
        throw new UnsupportedOperationException();
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapValueHolder
    void writeBack() {
        throw new UnsupportedOperationException();
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapValueHolder
    void forceDeserialization() {
        throw new UnsupportedOperationException();
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapValueHolder
    void detach() {
        throw new UnsupportedOperationException();
    }

    @Override // org.ehcache.ValueSupplier
    public V value() {
        return this.value;
    }
}
