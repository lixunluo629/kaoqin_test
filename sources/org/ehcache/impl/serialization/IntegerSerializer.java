package org.ehcache.impl.serialization;

import java.nio.ByteBuffer;
import org.ehcache.spi.serialization.Serializer;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/IntegerSerializer.class */
public class IntegerSerializer implements Serializer<Integer> {
    public IntegerSerializer() {
    }

    public IntegerSerializer(ClassLoader classLoader) {
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public ByteBuffer serialize(Integer object) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(object.intValue()).flip();
        return byteBuffer;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ehcache.spi.serialization.Serializer
    public Integer read(ByteBuffer binary) throws ClassNotFoundException {
        int i = binary.getInt();
        return Integer.valueOf(i);
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public boolean equals(Integer object, ByteBuffer binary) throws ClassNotFoundException {
        return object.equals(read(binary));
    }
}
