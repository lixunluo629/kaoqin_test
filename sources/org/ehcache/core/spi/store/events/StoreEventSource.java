package org.ehcache.core.spi.store.events;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/events/StoreEventSource.class */
public interface StoreEventSource<K, V> {
    void addEventListener(StoreEventListener<K, V> storeEventListener);

    void removeEventListener(StoreEventListener<K, V> storeEventListener);

    void addEventFilter(StoreEventFilter<K, V> storeEventFilter);

    void setEventOrdering(boolean z);

    boolean isEventOrdering();
}
