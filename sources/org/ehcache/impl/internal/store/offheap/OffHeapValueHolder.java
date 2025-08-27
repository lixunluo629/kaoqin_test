package org.ehcache.impl.internal.store.offheap;

import java.util.concurrent.TimeUnit;
import org.ehcache.core.spi.store.AbstractValueHolder;
import org.ehcache.core.spi.store.Store;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/OffHeapValueHolder.class */
public abstract class OffHeapValueHolder<V> extends AbstractValueHolder<V> {
    public static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    abstract void updateMetadata(Store.ValueHolder<V> valueHolder);

    abstract void writeBack();

    abstract void forceDeserialization();

    abstract void detach();

    public OffHeapValueHolder(long id, long creationTime, long expireTime) {
        super(id, creationTime, expireTime);
    }

    @Override // org.ehcache.core.spi.store.AbstractValueHolder
    protected final TimeUnit nativeTimeUnit() {
        return TIME_UNIT;
    }

    @Override // org.ehcache.core.spi.store.AbstractValueHolder
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof OffHeapValueHolder)) {
            return false;
        }
        OffHeapValueHolder<?> that = (OffHeapValueHolder) other;
        if (super.equals(that)) {
            return value().equals(that.value());
        }
        return false;
    }

    @Override // org.ehcache.core.spi.store.AbstractValueHolder
    public int hashCode() {
        int result = (31 * 1) + value().hashCode();
        return (31 * result) + super.hashCode();
    }
}
