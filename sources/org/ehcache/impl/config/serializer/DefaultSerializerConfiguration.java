package org.ehcache.impl.config.serializer;

import org.ehcache.impl.internal.classes.ClassInstanceConfiguration;
import org.ehcache.spi.serialization.SerializationProvider;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/serializer/DefaultSerializerConfiguration.class */
public class DefaultSerializerConfiguration<T> extends ClassInstanceConfiguration<Serializer<T>> implements ServiceConfiguration<SerializationProvider> {
    private final Type type;

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/serializer/DefaultSerializerConfiguration$Type.class */
    public enum Type {
        KEY,
        VALUE
    }

    public DefaultSerializerConfiguration(Class<? extends Serializer<T>> clazz, Type type) {
        super(clazz, new Object[0]);
        this.type = type;
    }

    public DefaultSerializerConfiguration(Serializer<T> serializer, Type type) {
        super(serializer);
        this.type = type;
    }

    @Override // org.ehcache.spi.service.ServiceConfiguration
    public Class<SerializationProvider> getServiceType() {
        return SerializationProvider.class;
    }

    public Type getType() {
        return this.type;
    }
}
