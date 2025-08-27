package org.ehcache.config;

import java.util.Set;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.event.EventType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/CacheRuntimeConfiguration.class */
public interface CacheRuntimeConfiguration<K, V> extends CacheConfiguration<K, V> {
    void registerCacheEventListener(CacheEventListener<? super K, ? super V> cacheEventListener, EventOrdering eventOrdering, EventFiring eventFiring, Set<EventType> set);

    void registerCacheEventListener(CacheEventListener<? super K, ? super V> cacheEventListener, EventOrdering eventOrdering, EventFiring eventFiring, EventType eventType, EventType... eventTypeArr);

    void deregisterCacheEventListener(CacheEventListener<? super K, ? super V> cacheEventListener);

    void updateResourcePools(ResourcePools resourcePools);
}
