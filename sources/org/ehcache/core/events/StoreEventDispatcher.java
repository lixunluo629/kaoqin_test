package org.ehcache.core.events;

import org.ehcache.core.spi.store.events.StoreEventSource;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/StoreEventDispatcher.class */
public interface StoreEventDispatcher<K, V> extends StoreEventSource<K, V> {
    StoreEventSink<K, V> eventSink();

    void releaseEventSink(StoreEventSink<K, V> storeEventSink);

    void releaseEventSinkAfterFailure(StoreEventSink<K, V> storeEventSink, Throwable th);

    void reset(StoreEventSink<K, V> storeEventSink);
}
