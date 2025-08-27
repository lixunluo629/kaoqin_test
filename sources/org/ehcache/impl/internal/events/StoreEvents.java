package org.ehcache.impl.internal.events;

import org.ehcache.core.spi.store.events.StoreEvent;
import org.ehcache.event.EventType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/StoreEvents.class */
public class StoreEvents {
    public static <K, V> StoreEvent<K, V> createEvent(K key, V value) {
        return new StoreEventImpl(EventType.CREATED, key, null, value);
    }

    public static <K, V> StoreEvent<K, V> updateEvent(K key, V oldValue, V newValue) {
        return new StoreEventImpl(EventType.UPDATED, key, oldValue, newValue);
    }

    public static <K, V> StoreEvent<K, V> removeEvent(K key, V oldValue) {
        return new StoreEventImpl(EventType.REMOVED, key, oldValue, null);
    }

    public static <K, V> StoreEvent<K, V> expireEvent(K key, V oldValue) {
        return new StoreEventImpl(EventType.EXPIRED, key, oldValue, null);
    }

    public static <K, V> StoreEvent<K, V> evictEvent(K key, V oldValue) {
        return new StoreEventImpl(EventType.EVICTED, key, oldValue, null);
    }
}
