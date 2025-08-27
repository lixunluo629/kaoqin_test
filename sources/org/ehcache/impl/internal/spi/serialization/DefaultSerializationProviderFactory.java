package org.ehcache.impl.internal.spi.serialization;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.config.serializer.DefaultSerializationProviderConfiguration;
import org.ehcache.spi.serialization.SerializationProvider;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/spi/serialization/DefaultSerializationProviderFactory.class */
public class DefaultSerializationProviderFactory implements ServiceFactory<SerializationProvider> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<SerializationProvider>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public DefaultSerializationProvider create(ServiceCreationConfiguration<SerializationProvider> configuration) {
        if (configuration != null && !(configuration instanceof DefaultSerializationProviderConfiguration)) {
            throw new IllegalArgumentException("Expected a configuration of type DefaultSerializationProviderConfiguration but got " + configuration.getClass().getSimpleName());
        }
        return new DefaultSerializationProvider((DefaultSerializationProviderConfiguration) configuration);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<SerializationProvider> getServiceType() {
        return SerializationProvider.class;
    }
}
