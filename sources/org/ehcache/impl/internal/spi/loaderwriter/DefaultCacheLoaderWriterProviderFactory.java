package org.ehcache.impl.internal.spi.loaderwriter;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.config.loaderwriter.DefaultCacheLoaderWriterProviderConfiguration;
import org.ehcache.spi.loaderwriter.CacheLoaderWriterProvider;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/spi/loaderwriter/DefaultCacheLoaderWriterProviderFactory.class */
public class DefaultCacheLoaderWriterProviderFactory implements ServiceFactory<CacheLoaderWriterProvider> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<CacheLoaderWriterProvider>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public DefaultCacheLoaderWriterProvider create(ServiceCreationConfiguration<CacheLoaderWriterProvider> configuration) {
        if (configuration != null && !(configuration instanceof DefaultCacheLoaderWriterProviderConfiguration)) {
            throw new IllegalArgumentException("Expected a configuration of type DefaultCacheLoaderWriterProviderConfiguration but got " + configuration.getClass().getSimpleName());
        }
        return new DefaultCacheLoaderWriterProvider((DefaultCacheLoaderWriterProviderConfiguration) configuration);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<CacheLoaderWriterProvider> getServiceType() {
        return CacheLoaderWriterProvider.class;
    }
}
