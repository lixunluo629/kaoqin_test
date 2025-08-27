package org.ehcache.spi.serialization;

import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/serialization/Serializer.class */
public interface Serializer<T> {
    ByteBuffer serialize(T t) throws SerializerException;

    T read(ByteBuffer byteBuffer) throws ClassNotFoundException, SerializerException;

    boolean equals(T t, ByteBuffer byteBuffer) throws ClassNotFoundException, SerializerException;
}
