package org.ehcache.core.events;

import org.ehcache.event.CacheEventListener;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheEventListenerProvider.class */
public interface CacheEventListenerProvider extends Service {
    <K, V> CacheEventListener<K, V> createEventListener(String str, ServiceConfiguration<CacheEventListenerProvider> serviceConfiguration);

    void releaseEventListener(CacheEventListener<?, ?> cacheEventListener) throws Exception;
}
