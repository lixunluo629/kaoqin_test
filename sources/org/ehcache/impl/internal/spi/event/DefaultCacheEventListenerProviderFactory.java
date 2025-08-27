package org.ehcache.impl.internal.spi.event;

import org.ehcache.core.events.CacheEventListenerProvider;
import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/spi/event/DefaultCacheEventListenerProviderFactory.class */
public class DefaultCacheEventListenerProviderFactory implements ServiceFactory<CacheEventListenerProvider> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<CacheEventListenerProvider>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public DefaultCacheEventListenerProvider create(ServiceCreationConfiguration<CacheEventListenerProvider> configuration) {
        return new DefaultCacheEventListenerProvider();
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<CacheEventListenerProvider> getServiceType() {
        return CacheEventListenerProvider.class;
    }
}
