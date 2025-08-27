package org.ehcache.impl.internal.spi.event;

import org.ehcache.core.events.CacheEventListenerProvider;
import org.ehcache.event.CacheEventListener;
import org.ehcache.impl.config.event.DefaultCacheEventListenerConfiguration;
import org.ehcache.impl.internal.classes.ClassInstanceProvider;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/spi/event/DefaultCacheEventListenerProvider.class */
public class DefaultCacheEventListenerProvider extends ClassInstanceProvider<String, CacheEventListener<?, ?>> implements CacheEventListenerProvider {
    public DefaultCacheEventListenerProvider() {
        super(null, DefaultCacheEventListenerConfiguration.class);
    }

    @Override // org.ehcache.core.events.CacheEventListenerProvider
    public <K, V> CacheEventListener<K, V> createEventListener(String alias, ServiceConfiguration<CacheEventListenerProvider> serviceConfiguration) {
        return (CacheEventListener) newInstance((DefaultCacheEventListenerProvider) alias, (ServiceConfiguration<?>) serviceConfiguration);
    }

    @Override // org.ehcache.core.events.CacheEventListenerProvider
    public void releaseEventListener(CacheEventListener<?, ?> cacheEventListener) throws Exception {
        releaseInstance(cacheEventListener);
    }
}
