package org.ehcache.core.events;

import org.ehcache.ValueSupplier;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/StoreEventSink.class */
public interface StoreEventSink<K, V> {
    void removed(K k, ValueSupplier<V> valueSupplier);

    void updated(K k, ValueSupplier<V> valueSupplier, V v);

    void expired(K k, ValueSupplier<V> valueSupplier);

    void created(K k, V v);

    void evicted(K k, ValueSupplier<V> valueSupplier);
}
