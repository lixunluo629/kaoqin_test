package org.ehcache.impl.internal.events;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.ehcache.Cache;
import org.ehcache.core.CacheConfigurationChangeListener;
import org.ehcache.core.events.CacheEventDispatcher;
import org.ehcache.core.spi.store.events.StoreEventSource;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.event.EventType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/DisabledCacheEventNotificationService.class */
public class DisabledCacheEventNotificationService<K, V> implements CacheEventDispatcher<K, V> {
    @Override // org.ehcache.core.events.CacheEventDispatcher
    public void registerCacheEventListener(CacheEventListener<? super K, ? super V> listener, EventOrdering ordering, EventFiring firing, EnumSet<EventType> eventTypes) {
    }

    @Override // org.ehcache.core.events.CacheEventDispatcher
    public void deregisterCacheEventListener(CacheEventListener<? super K, ? super V> listener) {
    }

    @Override // org.ehcache.core.events.CacheEventDispatcher
    public void shutdown() {
    }

    @Override // org.ehcache.core.events.CacheEventDispatcher
    public void setListenerSource(Cache<K, V> source) {
    }

    @Override // org.ehcache.core.spi.store.ConfigurationChangeSupport
    public List<CacheConfigurationChangeListener> getConfigurationChangeListeners() {
        return Collections.emptyList();
    }

    @Override // org.ehcache.core.events.CacheEventDispatcher
    public void setStoreEventSource(StoreEventSource<K, V> eventSource) {
    }
}
