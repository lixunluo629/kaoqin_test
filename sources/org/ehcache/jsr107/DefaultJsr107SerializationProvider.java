package org.ehcache.jsr107;

import org.ehcache.impl.config.serializer.DefaultSerializationProviderConfiguration;
import org.ehcache.impl.internal.spi.serialization.DefaultSerializationProvider;
import org.ehcache.impl.serialization.PlainJavaSerializer;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/DefaultJsr107SerializationProvider.class */
class DefaultJsr107SerializationProvider extends DefaultSerializationProvider {
    DefaultJsr107SerializationProvider() {
        super(new DefaultSerializationProviderConfiguration().addSerializerFor(Object.class, PlainJavaSerializer.class));
    }
}
