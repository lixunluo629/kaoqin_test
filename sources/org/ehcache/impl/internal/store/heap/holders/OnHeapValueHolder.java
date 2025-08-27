package org.ehcache.impl.internal.store.heap.holders;

import java.util.concurrent.TimeUnit;
import org.ehcache.core.spi.store.AbstractValueHolder;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/holders/OnHeapValueHolder.class */
public abstract class OnHeapValueHolder<V> extends AbstractValueHolder<V> {
    public static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    private final boolean evictionAdvice;
    private long size;

    protected OnHeapValueHolder(long id, long creationTime, boolean evictionAdvice) {
        super(id, creationTime);
        this.evictionAdvice = evictionAdvice;
    }

    protected OnHeapValueHolder(long id, long creationTime, long expirationTime, boolean evictionAdvice) {
        super(id, creationTime, expirationTime);
        this.evictionAdvice = evictionAdvice;
    }

    public boolean evictionAdvice() {
        return this.evictionAdvice;
    }

    public long size() {
        return this.size;
    }

    public void setSize(long size) {
        if (this.size != 0) {
            throw new UnsupportedOperationException("Cannot change the size if it is done already");
        }
        this.size = size;
    }

    @Override // org.ehcache.core.spi.store.AbstractValueHolder
    protected final TimeUnit nativeTimeUnit() {
        return TIME_UNIT;
    }

    @Override // org.ehcache.core.spi.store.AbstractValueHolder
    public boolean equals(Object obj) {
        if (obj != null && getClass().equals(obj.getClass())) {
            return super.equals(obj);
        }
        return false;
    }
}
