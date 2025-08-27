package org.ehcache.core.spi.store.events;

import org.ehcache.event.EventType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/events/StoreEvent.class */
public interface StoreEvent<K, V> {
    EventType getType();

    K getKey();

    V getNewValue();

    V getOldValue();
}
