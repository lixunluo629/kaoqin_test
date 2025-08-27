package org.ehcache.impl.internal.store.offheap.portability;

import java.nio.ByteBuffer;
import org.ehcache.impl.internal.store.BinaryValueHolder;
import org.ehcache.impl.internal.store.offheap.LazyOffHeapValueHolder;
import org.ehcache.impl.internal.store.offheap.OffHeapValueHolder;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;
import org.terracotta.offheapstore.storage.portability.WriteBackPortability;
import org.terracotta.offheapstore.storage.portability.WriteContext;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/portability/OffHeapValueHolderPortability.class */
public class OffHeapValueHolderPortability<V> implements WriteBackPortability<OffHeapValueHolder<V>> {
    public static final int ACCESS_TIME_OFFSET = 16;
    public static final int EXPIRE_TIME_OFFSET = 24;
    public static final int HITS_OFFSET = 32;
    private static final int FIELDS_OVERHEAD = 40;
    private final Serializer<V> serializer;

    public OffHeapValueHolderPortability(Serializer<V> serializer) {
        this.serializer = serializer;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public ByteBuffer encode(OffHeapValueHolder<V> offHeapValueHolder) throws IllegalStateException, SerializerException {
        ByteBuffer byteBufferSerialize;
        if ((offHeapValueHolder instanceof BinaryValueHolder) && ((BinaryValueHolder) offHeapValueHolder).isBinaryValueAvailable()) {
            byteBufferSerialize = ((BinaryValueHolder) offHeapValueHolder).getBinaryValue();
        } else {
            byteBufferSerialize = this.serializer.serialize(offHeapValueHolder.value());
        }
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(byteBufferSerialize.remaining() + 40);
        byteBufferAllocate.putLong(offHeapValueHolder.getId());
        byteBufferAllocate.putLong(offHeapValueHolder.creationTime(OffHeapValueHolder.TIME_UNIT));
        byteBufferAllocate.putLong(offHeapValueHolder.lastAccessTime(OffHeapValueHolder.TIME_UNIT));
        byteBufferAllocate.putLong(offHeapValueHolder.expirationTime(OffHeapValueHolder.TIME_UNIT));
        byteBufferAllocate.putLong(offHeapValueHolder.hits());
        byteBufferAllocate.put(byteBufferSerialize);
        byteBufferAllocate.flip();
        return byteBufferAllocate;
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public OffHeapValueHolder<V> decode(ByteBuffer byteBuffer) {
        return decode(byteBuffer, (WriteContext) null);
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public boolean equals(Object o, ByteBuffer byteBuffer) {
        return o.equals(decode(byteBuffer));
    }

    @Override // org.terracotta.offheapstore.storage.portability.WriteBackPortability
    public OffHeapValueHolder<V> decode(ByteBuffer byteBuffer, WriteContext writeContext) {
        long id = byteBuffer.getLong();
        long creationTime = byteBuffer.getLong();
        long lastAccessTime = byteBuffer.getLong();
        long expireTime = byteBuffer.getLong();
        long hits = byteBuffer.getLong();
        return new LazyOffHeapValueHolder(id, byteBuffer.slice(), this.serializer, creationTime, expireTime, lastAccessTime, hits, writeContext);
    }
}
