package org.ehcache.core.events;

import org.ehcache.ValueSupplier;
import org.ehcache.core.spi.store.events.StoreEventFilter;
import org.ehcache.core.spi.store.events.StoreEventListener;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/NullStoreEventDispatcher.class */
public class NullStoreEventDispatcher<K, V> implements StoreEventDispatcher<K, V> {
    private final StoreEventSink<K, V> storeEventSink = new StoreEventSink<K, V>() { // from class: org.ehcache.core.events.NullStoreEventDispatcher.1
        @Override // org.ehcache.core.events.StoreEventSink
        public void evicted(K key, ValueSupplier<V> value) {
        }

        @Override // org.ehcache.core.events.StoreEventSink
        public void expired(K key, ValueSupplier<V> value) {
        }

        @Override // org.ehcache.core.events.StoreEventSink
        public void created(K key, V value) {
        }

        @Override // org.ehcache.core.events.StoreEventSink
        public void updated(K key, ValueSupplier<V> previousValue, V newValue) {
        }

        @Override // org.ehcache.core.events.StoreEventSink
        public void removed(K key, ValueSupplier<V> removed) {
        }
    };

    public static <K, V> StoreEventDispatcher<K, V> nullStoreEventDispatcher() {
        return new NullStoreEventDispatcher();
    }

    @Override // org.ehcache.core.events.StoreEventDispatcher
    public StoreEventSink<K, V> eventSink() {
        return this.storeEventSink;
    }

    @Override // org.ehcache.core.events.StoreEventDispatcher
    public void releaseEventSink(StoreEventSink<K, V> eventSink) {
    }

    @Override // org.ehcache.core.events.StoreEventDispatcher
    public void releaseEventSinkAfterFailure(StoreEventSink<K, V> eventSink, Throwable throwable) {
    }

    @Override // org.ehcache.core.events.StoreEventDispatcher
    public void reset(StoreEventSink<K, V> eventSink) {
    }

    @Override // org.ehcache.core.spi.store.events.StoreEventSource
    public void addEventListener(StoreEventListener<K, V> eventListener) {
    }

    @Override // org.ehcache.core.spi.store.events.StoreEventSource
    public void removeEventListener(StoreEventListener<K, V> eventListener) {
    }

    @Override // org.ehcache.core.spi.store.events.StoreEventSource
    public void addEventFilter(StoreEventFilter<K, V> eventFilter) {
    }

    @Override // org.ehcache.core.spi.store.events.StoreEventSource
    public void setEventOrdering(boolean ordering) {
    }

    @Override // org.ehcache.core.spi.store.events.StoreEventSource
    public boolean isEventOrdering() {
        return false;
    }
}
