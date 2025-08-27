package org.ehcache.impl.internal.store.heap.holders;

import java.nio.ByteBuffer;
import org.ehcache.core.spi.store.Store;
import org.ehcache.expiry.Duration;
import org.ehcache.impl.internal.store.BinaryValueHolder;
import org.ehcache.sizeof.annotations.IgnoreSizeOf;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/holders/SerializedOnHeapValueHolder.class */
public class SerializedOnHeapValueHolder<V> extends OnHeapValueHolder<V> implements BinaryValueHolder {
    private final ByteBuffer buffer;

    @IgnoreSizeOf
    private final Serializer<V> serializer;

    protected SerializedOnHeapValueHolder(long id, V value, long creationTime, long expirationTime, boolean evictionAdvice, Serializer<V> serializer) {
        super(id, creationTime, expirationTime, evictionAdvice);
        if (value == null) {
            throw new NullPointerException("null value");
        }
        if (serializer == null) {
            throw new NullPointerException("null serializer");
        }
        this.serializer = serializer;
        this.buffer = serializer.serialize(value).asReadOnlyBuffer();
    }

    public SerializedOnHeapValueHolder(V value, long creationTime, boolean evictionAdvice, Serializer<V> serializer) {
        this(value, creationTime, -1L, evictionAdvice, serializer);
    }

    public SerializedOnHeapValueHolder(V value, long creationTime, long expirationTime, boolean evictionAdvice, Serializer<V> serializer) {
        this(-1L, value, creationTime, expirationTime, evictionAdvice, serializer);
    }

    public SerializedOnHeapValueHolder(Store.ValueHolder<V> valueHolder, V value, boolean evictionAdvice, Serializer<V> serializer, long now, Duration expiration) {
        this(valueHolder.getId(), value, valueHolder.creationTime(TIME_UNIT), valueHolder.expirationTime(TIME_UNIT), evictionAdvice, serializer);
        setHits(valueHolder.hits());
        accessed(now, expiration);
    }

    public SerializedOnHeapValueHolder(Store.ValueHolder<V> valueHolder, ByteBuffer binaryValue, boolean evictionAdvice, Serializer<V> serializer, long now, Duration expiration) {
        super(valueHolder.getId(), valueHolder.creationTime(TIME_UNIT), valueHolder.expirationTime(TIME_UNIT), evictionAdvice);
        this.buffer = binaryValue;
        this.serializer = serializer;
        setHits(valueHolder.hits());
        accessed(now, expiration);
    }

    @Override // org.ehcache.ValueSupplier
    public final V value() {
        try {
            return this.serializer.read(this.buffer.duplicate());
        } catch (ClassNotFoundException cnfe) {
            throw new SerializerException(cnfe);
        }
    }

    @Override // org.ehcache.impl.internal.store.BinaryValueHolder
    public ByteBuffer getBinaryValue() throws IllegalStateException {
        return this.buffer.duplicate();
    }

    @Override // org.ehcache.impl.internal.store.BinaryValueHolder
    public boolean isBinaryValueAvailable() {
        return true;
    }

    @Override // org.ehcache.impl.internal.store.heap.holders.OnHeapValueHolder, org.ehcache.core.spi.store.AbstractValueHolder
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SerializedOnHeapValueHolder serializedOnHeapValueHolder = (SerializedOnHeapValueHolder) obj;
        if (!super.equals(serializedOnHeapValueHolder)) {
            return false;
        }
        try {
            return this.serializer.equals(serializedOnHeapValueHolder.value(), this.buffer);
        } catch (ClassNotFoundException e) {
            throw new SerializerException(e);
        }
    }

    @Override // org.ehcache.core.spi.store.AbstractValueHolder
    public int hashCode() {
        int result = (31 * 1) + super.hashCode();
        return result;
    }
}
