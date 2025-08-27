package org.ehcache.impl.internal.events;

import org.ehcache.core.events.StoreEventSink;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/CloseableStoreEventSink.class */
interface CloseableStoreEventSink<K, V> extends StoreEventSink<K, V> {
    void close();

    void closeOnFailure();

    void reset();
}
