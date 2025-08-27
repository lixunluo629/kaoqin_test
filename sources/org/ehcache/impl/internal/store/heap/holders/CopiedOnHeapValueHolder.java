package org.ehcache.impl.internal.store.heap.holders;

import org.ehcache.core.spi.store.Store;
import org.ehcache.expiry.Duration;
import org.ehcache.sizeof.annotations.IgnoreSizeOf;
import org.ehcache.spi.copy.Copier;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/holders/CopiedOnHeapValueHolder.class */
public class CopiedOnHeapValueHolder<V> extends OnHeapValueHolder<V> {
    private final V copiedValue;

    @IgnoreSizeOf
    private final Copier<V> valueCopier;

    protected CopiedOnHeapValueHolder(long id, V value, long creationTime, long expirationTime, boolean evictionAdvice, Copier<V> valueCopier) {
        super(id, creationTime, expirationTime, evictionAdvice);
        if (value == null) {
            throw new NullPointerException("null value");
        }
        if (valueCopier == null) {
            throw new NullPointerException("null copier");
        }
        this.valueCopier = valueCopier;
        this.copiedValue = valueCopier.copyForWrite(value);
    }

    public CopiedOnHeapValueHolder(Store.ValueHolder<V> valueHolder, V value, boolean evictionAdvice, Copier<V> valueCopier, long now, Duration expiration) {
        super(valueHolder.getId(), valueHolder.creationTime(TIME_UNIT), valueHolder.expirationTime(TIME_UNIT), evictionAdvice);
        if (value == null) {
            throw new NullPointerException("null value");
        }
        if (valueCopier == null) {
            throw new NullPointerException("null copier");
        }
        this.valueCopier = valueCopier;
        this.copiedValue = value;
        setHits(valueHolder.hits());
        accessed(now, expiration);
    }

    public CopiedOnHeapValueHolder(V value, long creationTime, boolean evictionAdvice, Copier<V> valueCopier) {
        this(value, creationTime, -1L, evictionAdvice, valueCopier);
    }

    public CopiedOnHeapValueHolder(V value, long creationTime, long expirationTime, boolean evictionAdvice, Copier<V> valueCopier) {
        this(-1L, value, creationTime, expirationTime, evictionAdvice, valueCopier);
    }

    @Override // org.ehcache.ValueSupplier
    public V value() {
        return this.valueCopier.copyForRead(this.copiedValue);
    }
}
