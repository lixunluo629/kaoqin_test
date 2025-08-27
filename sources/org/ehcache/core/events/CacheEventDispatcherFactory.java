package org.ehcache.core.events;

import org.ehcache.core.spi.store.Store;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheEventDispatcherFactory.class */
public interface CacheEventDispatcherFactory extends Service {
    <K, V> CacheEventDispatcher<K, V> createCacheEventDispatcher(Store<K, V> store, ServiceConfiguration<?>... serviceConfigurationArr);

    <K, V> void releaseCacheEventDispatcher(CacheEventDispatcher<K, V> cacheEventDispatcher);
}
