package org.ehcache.impl.serialization;

import java.nio.ByteBuffer;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/ByteArraySerializer.class */
public class ByteArraySerializer implements Serializer<byte[]> {
    public ByteArraySerializer() {
    }

    public ByteArraySerializer(ClassLoader classLoader) {
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public ByteBuffer serialize(byte[] object) throws SerializerException {
        return ByteBuffer.wrap(object);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ehcache.spi.serialization.Serializer
    public byte[] read(ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        byte[] bytes = new byte[binary.remaining()];
        binary.get(bytes);
        return bytes;
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public boolean equals(byte[] object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        boolean equals = binary.equals(serialize(object));
        binary.position(binary.limit());
        return equals;
    }
}
