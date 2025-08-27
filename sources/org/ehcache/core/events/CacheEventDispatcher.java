package org.ehcache.core.events;

import java.util.EnumSet;
import org.ehcache.Cache;
import org.ehcache.core.spi.store.ConfigurationChangeSupport;
import org.ehcache.core.spi.store.events.StoreEventSource;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.event.EventType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheEventDispatcher.class */
public interface CacheEventDispatcher<K, V> extends ConfigurationChangeSupport {
    void registerCacheEventListener(CacheEventListener<? super K, ? super V> cacheEventListener, EventOrdering eventOrdering, EventFiring eventFiring, EnumSet<EventType> enumSet);

    void deregisterCacheEventListener(CacheEventListener<? super K, ? super V> cacheEventListener);

    void shutdown();

    void setListenerSource(Cache<K, V> cache);

    void setStoreEventSource(StoreEventSource<K, V> storeEventSource);
}
