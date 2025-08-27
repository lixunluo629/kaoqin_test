package org.ehcache.impl.internal.store.offheap.portability;

import java.nio.ByteBuffer;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;
import org.terracotta.offheapstore.storage.portability.Portability;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/portability/SerializerPortability.class */
public class SerializerPortability<T> implements Portability<T> {
    private final Serializer<T> serializer;

    public SerializerPortability(Serializer<T> serializer) {
        this.serializer = serializer;
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public ByteBuffer encode(T t) {
        return this.serializer.serialize(t);
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public T decode(ByteBuffer byteBuffer) {
        try {
            return this.serializer.read(byteBuffer);
        } catch (ClassNotFoundException e) {
            throw new SerializerException(e);
        }
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public boolean equals(Object o, ByteBuffer byteBuffer) {
        try {
            return this.serializer.equals(o, byteBuffer);
        } catch (ClassNotFoundException e) {
            throw new SerializerException(e);
        }
    }
}
