package org.ehcache.impl.config.event;

import org.ehcache.core.config.store.StoreEventSourceConfiguration;
import org.ehcache.core.spi.store.Store;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/event/DefaultEventSourceConfiguration.class */
public class DefaultEventSourceConfiguration implements StoreEventSourceConfiguration {
    private final int dispatcherConcurrency;

    public DefaultEventSourceConfiguration(int dispatcherConcurrency) {
        if (dispatcherConcurrency <= 0) {
            throw new IllegalArgumentException("Dispatcher concurrency must be a value bigger than 0");
        }
        this.dispatcherConcurrency = dispatcherConcurrency;
    }

    @Override // org.ehcache.core.config.store.StoreEventSourceConfiguration
    public int getDispatcherConcurrency() {
        return this.dispatcherConcurrency;
    }

    @Override // org.ehcache.spi.service.ServiceConfiguration
    public Class<Store.Provider> getServiceType() {
        return Store.Provider.class;
    }
}
