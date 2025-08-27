package org.ehcache.impl.config.event;

import org.ehcache.core.events.CacheEventDispatcherFactory;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/event/DefaultCacheEventDispatcherConfiguration.class */
public class DefaultCacheEventDispatcherConfiguration implements ServiceConfiguration<CacheEventDispatcherFactory> {
    private final String threadPoolAlias;

    public DefaultCacheEventDispatcherConfiguration(String threadPoolAlias) {
        this.threadPoolAlias = threadPoolAlias;
    }

    @Override // org.ehcache.spi.service.ServiceConfiguration
    public Class<CacheEventDispatcherFactory> getServiceType() {
        return CacheEventDispatcherFactory.class;
    }

    public String getThreadPoolAlias() {
        return this.threadPoolAlias;
    }
}
