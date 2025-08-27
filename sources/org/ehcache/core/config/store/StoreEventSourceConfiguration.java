package org.ehcache.core.config.store;

import org.ehcache.core.spi.store.Store;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/config/store/StoreEventSourceConfiguration.class */
public interface StoreEventSourceConfiguration extends ServiceConfiguration<Store.Provider> {
    public static final int DEFAULT_DISPATCHER_CONCURRENCY = 1;

    int getDispatcherConcurrency();
}
