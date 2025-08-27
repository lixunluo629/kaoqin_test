package org.ehcache.impl.serialization;

import java.nio.ByteBuffer;
import org.ehcache.spi.serialization.Serializer;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/FloatSerializer.class */
public class FloatSerializer implements Serializer<Float> {
    public FloatSerializer() {
    }

    public FloatSerializer(ClassLoader classLoader) {
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public ByteBuffer serialize(Float object) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putFloat(object.floatValue()).flip();
        return byteBuffer;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ehcache.spi.serialization.Serializer
    public Float read(ByteBuffer binary) throws ClassNotFoundException {
        float f = binary.getFloat();
        return Float.valueOf(f);
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public boolean equals(Float object, ByteBuffer binary) throws ClassNotFoundException {
        return object.equals(read(binary));
    }
}
