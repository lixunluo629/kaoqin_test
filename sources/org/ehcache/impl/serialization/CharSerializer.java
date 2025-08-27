package org.ehcache.impl.serialization;

import java.nio.ByteBuffer;
import org.ehcache.spi.serialization.Serializer;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/CharSerializer.class */
public class CharSerializer implements Serializer<Character> {
    public CharSerializer() {
    }

    public CharSerializer(ClassLoader classLoader) {
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public ByteBuffer serialize(Character object) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        byteBuffer.putChar(object.charValue()).flip();
        return byteBuffer;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ehcache.spi.serialization.Serializer
    public Character read(ByteBuffer binary) throws ClassNotFoundException {
        char c = binary.getChar();
        return Character.valueOf(c);
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public boolean equals(Character object, ByteBuffer binary) throws ClassNotFoundException {
        return object.equals(read(binary));
    }
}
