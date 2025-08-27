package org.ehcache.spi.serialization;

import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/serialization/SerializationProvider.class */
public interface SerializationProvider extends Service {
    <T> Serializer<T> createKeySerializer(Class<T> cls, ClassLoader classLoader, ServiceConfiguration<?>... serviceConfigurationArr) throws UnsupportedTypeException;

    <T> Serializer<T> createValueSerializer(Class<T> cls, ClassLoader classLoader, ServiceConfiguration<?>... serviceConfigurationArr) throws UnsupportedTypeException;

    void releaseSerializer(Serializer<?> serializer) throws Exception;
}
