package org.ehcache.impl.internal.store.offheap;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import org.ehcache.core.spi.store.Store;
import org.ehcache.impl.internal.store.BinaryValueHolder;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/BinaryOffHeapValueHolder.class */
final class BinaryOffHeapValueHolder<V> extends OffHeapValueHolder<V> implements BinaryValueHolder {
    private final ByteBuffer binaryValue;
    private final V value;

    BinaryOffHeapValueHolder(long id, V value, ByteBuffer binaryValue, long creationTime, long expireTime, long lastAccessTime, long hits) {
        super(id, creationTime, expireTime);
        this.value = value;
        setLastAccessTime(lastAccessTime, TIME_UNIT);
        this.binaryValue = binaryValue;
        setHits(hits);
    }

    @Override // org.ehcache.impl.internal.store.BinaryValueHolder
    public ByteBuffer getBinaryValue() throws IllegalStateException {
        return this.binaryValue.duplicate();
    }

    @Override // org.ehcache.impl.internal.store.BinaryValueHolder
    public boolean isBinaryValueAvailable() {
        return true;
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

    private void writeObject(ObjectOutputStream out) throws IOException {
        throw new UnsupportedOperationException("This subclass of AbstractValueHolder is NOT serializable");
    }
}
