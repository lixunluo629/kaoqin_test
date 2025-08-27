package org.ehcache.impl.serialization;

import java.nio.ByteBuffer;
import org.ehcache.spi.serialization.Serializer;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/DoubleSerializer.class */
public class DoubleSerializer implements Serializer<Double> {
    public DoubleSerializer() {
    }

    public DoubleSerializer(ClassLoader classLoader) {
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public ByteBuffer serialize(Double object) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putDouble(object.doubleValue()).flip();
        return byteBuffer;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ehcache.spi.serialization.Serializer
    public Double read(ByteBuffer binary) throws ClassNotFoundException {
        double d = binary.getDouble();
        return Double.valueOf(d);
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public boolean equals(Double object, ByteBuffer binary) throws ClassNotFoundException {
        return object.equals(read(binary));
    }
}
