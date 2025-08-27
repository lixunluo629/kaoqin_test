package org.ehcache.impl.config.event;

import org.ehcache.core.events.CacheEventDispatcherFactory;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/event/CacheEventDispatcherFactoryConfiguration.class */
public class CacheEventDispatcherFactoryConfiguration implements ServiceCreationConfiguration<CacheEventDispatcherFactory> {
    private final String threadPoolAlias;

    public CacheEventDispatcherFactoryConfiguration(String threadPoolAlias) {
        this.threadPoolAlias = threadPoolAlias;
    }

    public String getThreadPoolAlias() {
        return this.threadPoolAlias;
    }

    @Override // org.ehcache.spi.service.ServiceCreationConfiguration
    public Class<CacheEventDispatcherFactory> getServiceType() {
        return CacheEventDispatcherFactory.class;
    }
}
