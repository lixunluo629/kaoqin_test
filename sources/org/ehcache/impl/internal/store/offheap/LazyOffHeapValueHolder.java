package org.ehcache.impl.internal.store.offheap;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import org.ehcache.core.spi.store.Store;
import org.ehcache.impl.internal.store.BinaryValueHolder;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;
import org.terracotta.offheapstore.storage.portability.WriteContext;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/LazyOffHeapValueHolder.class */
public final class LazyOffHeapValueHolder<V> extends OffHeapValueHolder<V> implements BinaryValueHolder {
    private final Serializer<V> valueSerializer;
    private final WriteContext writeContext;
    private Mode mode;
    private ByteBuffer binaryValue;
    private V value;

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/LazyOffHeapValueHolder$Mode.class */
    private enum Mode {
        ATTACHED,
        DETACHED
    }

    public LazyOffHeapValueHolder(long id, ByteBuffer binaryValue, Serializer<V> serializer, long creationTime, long expireTime, long lastAccessTime, long hits, WriteContext writeContext) {
        super(id, creationTime, expireTime);
        setLastAccessTime(lastAccessTime, TIME_UNIT);
        this.binaryValue = binaryValue;
        this.valueSerializer = serializer;
        setHits(hits);
        this.writeContext = writeContext;
        this.mode = Mode.ATTACHED;
    }

    @Override // org.ehcache.ValueSupplier
    public V value() {
        forceDeserialization();
        return this.value;
    }

    @Override // org.ehcache.impl.internal.store.BinaryValueHolder
    public ByteBuffer getBinaryValue() throws IllegalStateException {
        if (isBinaryValueAvailable()) {
            return this.binaryValue.duplicate();
        }
        throw new IllegalStateException("This OffHeapValueHolder has not been prepared to hand off its binary form");
    }

    @Override // org.ehcache.impl.internal.store.BinaryValueHolder
    public boolean isBinaryValueAvailable() {
        return this.mode == Mode.DETACHED;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapValueHolder
    void updateMetadata(Store.ValueHolder<V> valueFlushed) {
        if (getId() != valueFlushed.getId()) {
            throw new IllegalArgumentException("Wrong id passed in [this.id != id] : " + getId() + " != " + valueFlushed.getId());
        }
        setLastAccessTime(valueFlushed.lastAccessTime(TIME_UNIT), TIME_UNIT);
        setExpirationTime(valueFlushed.expirationTime(TIME_UNIT), TIME_UNIT);
        setHits(valueFlushed.hits());
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapValueHolder
    void writeBack() {
        this.writeContext.setLong(16, lastAccessTime(TimeUnit.MILLISECONDS));
        this.writeContext.setLong(24, expirationTime(TimeUnit.MILLISECONDS));
        this.writeContext.setLong(32, hits());
        this.writeContext.flush();
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapValueHolder
    void forceDeserialization() {
        if (this.value == null) {
            try {
                this.value = this.valueSerializer.read(this.binaryValue.duplicate());
            } catch (ClassNotFoundException e) {
                throw new SerializerException(e);
            } catch (SerializerException e2) {
                throw new SerializerException("Seeing this exception and having no other serialization related issues is a red flag!", e2);
            }
        }
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapValueHolder
    void detach() {
        if (this.mode == Mode.ATTACHED) {
            byte[] bytes = new byte[this.binaryValue.remaining()];
            this.binaryValue.get(bytes);
            this.binaryValue = ByteBuffer.wrap(bytes);
            this.mode = Mode.DETACHED;
            return;
        }
        throw new IllegalStateException("OffHeapValueHolder in mode " + this.mode + " cannot be prepared for delayed deserialization");
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        throw new UnsupportedOperationException("This subclass of AbstractValueHolder is NOT serializable");
    }
}
