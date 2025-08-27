package org.ehcache.impl.internal.sizeof;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineProviderConfiguration;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/sizeof/DefaultSizeOfEngineProviderFactory.class */
public class DefaultSizeOfEngineProviderFactory implements ServiceFactory<SizeOfEngineProvider> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<SizeOfEngineProvider>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public SizeOfEngineProvider create(ServiceCreationConfiguration<SizeOfEngineProvider> configuration) {
        long maxTraversals = 1000;
        long maxSize = Long.MAX_VALUE;
        if (configuration != null) {
            DefaultSizeOfEngineProviderConfiguration sizeOfEngineConfiguration = (DefaultSizeOfEngineProviderConfiguration) configuration;
            maxTraversals = sizeOfEngineConfiguration.getMaxObjectGraphSize();
            maxSize = sizeOfEngineConfiguration.getUnit().toBytes(sizeOfEngineConfiguration.getMaxObjectSize());
        }
        return new DefaultSizeOfEngineProvider(maxTraversals, maxSize);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<SizeOfEngineProvider> getServiceType() {
        return SizeOfEngineProvider.class;
    }
}
