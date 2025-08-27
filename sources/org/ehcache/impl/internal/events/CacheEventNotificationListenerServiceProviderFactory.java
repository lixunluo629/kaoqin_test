package org.ehcache.impl.internal.events;

import org.ehcache.core.events.CacheEventDispatcherFactory;
import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.config.event.CacheEventDispatcherFactoryConfiguration;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/CacheEventNotificationListenerServiceProviderFactory.class */
public class CacheEventNotificationListenerServiceProviderFactory implements ServiceFactory<CacheEventDispatcherFactory> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<CacheEventDispatcherFactory>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public CacheEventDispatcherFactory create(ServiceCreationConfiguration<CacheEventDispatcherFactory> configuration) {
        if (configuration == null) {
            return new CacheEventDispatcherFactoryImpl();
        }
        if (configuration instanceof CacheEventDispatcherFactoryConfiguration) {
            return new CacheEventDispatcherFactoryImpl((CacheEventDispatcherFactoryConfiguration) configuration);
        }
        throw new IllegalArgumentException("Expected a configuration of type CacheEventDispatcherFactoryConfiguration but got " + configuration.getClass().getSimpleName());
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<? extends CacheEventDispatcherFactory> getServiceType() {
        return CacheEventDispatcherFactoryImpl.class;
    }
}
