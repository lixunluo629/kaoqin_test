package org.ehcache.core.spi.store.events;

import org.ehcache.event.EventType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/events/StoreEventFilter.class */
public interface StoreEventFilter<K, V> {
    boolean acceptEvent(EventType eventType, K k, V v, V v2);
}
