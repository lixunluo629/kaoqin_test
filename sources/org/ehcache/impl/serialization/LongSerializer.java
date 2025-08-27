package org.ehcache.impl.serialization;

import java.nio.ByteBuffer;
import org.ehcache.spi.serialization.Serializer;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/LongSerializer.class */
public class LongSerializer implements Serializer<Long> {
    public LongSerializer() {
    }

    public LongSerializer(ClassLoader classLoader) {
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public ByteBuffer serialize(Long object) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putLong(object.longValue()).flip();
        return byteBuffer;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ehcache.spi.serialization.Serializer
    public Long read(ByteBuffer binary) throws ClassNotFoundException {
        long l = binary.getLong();
        return Long.valueOf(l);
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public boolean equals(Long object, ByteBuffer binary) throws ClassNotFoundException {
        return object.equals(read(binary));
    }
}
