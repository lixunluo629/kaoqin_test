package org.ehcache.impl.copy;

import org.ehcache.spi.copy.Copier;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/copy/SerializingCopier.class */
public final class SerializingCopier<T> extends ReadWriteCopier<T> {
    private final Serializer<T> serializer;

    public static <T> Class<? extends Copier<T>> asCopierClass() {
        return SerializingCopier.class;
    }

    public SerializingCopier(Serializer<T> serializer) {
        if (serializer == null) {
            throw new NullPointerException("A " + SerializingCopier.class.getName() + " instance requires a " + Serializer.class.getName() + " instance to copy!");
        }
        this.serializer = serializer;
    }

    @Override // org.ehcache.impl.copy.ReadWriteCopier
    public T copy(T obj) {
        try {
            return this.serializer.read(this.serializer.serialize(obj));
        } catch (ClassNotFoundException e) {
            throw new SerializerException("Copying failed.", e);
        }
    }

    public Serializer<T> getSerializer() {
        return this.serializer;
    }
}
