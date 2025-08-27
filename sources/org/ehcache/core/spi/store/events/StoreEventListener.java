package org.ehcache.core.spi.store.events;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/events/StoreEventListener.class */
public interface StoreEventListener<K, V> {
    void onEvent(StoreEvent<K, V> storeEvent);
}
