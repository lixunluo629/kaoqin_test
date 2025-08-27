package org.ehcache.impl.internal.events;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import org.ehcache.ValueSupplier;
import org.ehcache.core.events.StoreEventDispatcher;
import org.ehcache.core.events.StoreEventSink;
import org.ehcache.core.spi.store.events.StoreEventFilter;
import org.ehcache.core.spi.store.events.StoreEventListener;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/AbstractStoreEventDispatcher.class */
abstract class AbstractStoreEventDispatcher<K, V> implements StoreEventDispatcher<K, V> {
    protected static final StoreEventSink<?, ?> NO_OP_EVENT_SINK = new CloseableStoreEventSink<Object, Object>() { // from class: org.ehcache.impl.internal.events.AbstractStoreEventDispatcher.1
        @Override // org.ehcache.impl.internal.events.CloseableStoreEventSink
        public void close() {
        }

        @Override // org.ehcache.impl.internal.events.CloseableStoreEventSink
        public void closeOnFailure() {
        }

        @Override // org.ehcache.impl.internal.events.CloseableStoreEventSink
        public void reset() {
        }

        @Override // org.ehcache.core.events.StoreEventSink
        public void removed(Object key, ValueSupplier<Object> value) {
        }

        @Override // org.ehcache.core.events.StoreEventSink
        public void updated(Object key, ValueSupplier<Object> oldValue, Object newValue) {
        }

        @Override // org.ehcache.core.events.StoreEventSink
        public void expired(Object key, ValueSupplier<Object> value) {
        }

        @Override // org.ehcache.core.events.StoreEventSink
        public void created(Object key, Object value) {
        }

        @Override // org.ehcache.core.events.StoreEventSink
        public void evicted(Object key, ValueSupplier<Object> value) {
        }
    };
    private final BlockingQueue<FireableStoreEventHolder<K, V>>[] orderedQueues;
    private final Set<StoreEventFilter<K, V>> filters = new CopyOnWriteArraySet();
    private final Set<StoreEventListener<K, V>> listeners = new CopyOnWriteArraySet();
    private volatile boolean ordered = false;

    protected AbstractStoreEventDispatcher(int dispatcherConcurrency) {
        if (dispatcherConcurrency <= 0) {
            throw new IllegalArgumentException("Dispatcher concurrency must be an integer greater than 0");
        }
        LinkedBlockingQueue<FireableStoreEventHolder<K, V>>[] queues = new LinkedBlockingQueue[dispatcherConcurrency];
        this.orderedQueues = queues;
        for (int i = 0; i < this.orderedQueues.length; i++) {
            this.orderedQueues[i] = new LinkedBlockingQueue(10000);
        }
    }

    protected Set<StoreEventListener<K, V>> getListeners() {
        return this.listeners;
    }

    protected Set<StoreEventFilter<K, V>> getFilters() {
        return this.filters;
    }

    protected BlockingQueue<FireableStoreEventHolder<K, V>>[] getOrderedQueues() {
        return this.orderedQueues;
    }

    @Override // org.ehcache.core.spi.store.events.StoreEventSource
    public void addEventListener(StoreEventListener<K, V> eventListener) {
        this.listeners.add(eventListener);
    }

    @Override // org.ehcache.core.spi.store.events.StoreEventSource
    public void removeEventListener(StoreEventListener<K, V> eventListener) {
        this.listeners.remove(eventListener);
    }

    @Override // org.ehcache.core.spi.store.events.StoreEventSource
    public void addEventFilter(StoreEventFilter<K, V> eventFilter) {
        this.filters.add(eventFilter);
    }

    @Override // org.ehcache.core.spi.store.events.StoreEventSource
    public void setEventOrdering(boolean ordering) {
        this.ordered = ordering;
    }

    @Override // org.ehcache.core.spi.store.events.StoreEventSource
    public boolean isEventOrdering() {
        return this.ordered;
    }

    @Override // org.ehcache.core.events.StoreEventDispatcher
    public void releaseEventSink(StoreEventSink<K, V> eventSink) {
        ((CloseableStoreEventSink) eventSink).close();
    }

    @Override // org.ehcache.core.events.StoreEventDispatcher
    public void releaseEventSinkAfterFailure(StoreEventSink<K, V> eventSink, Throwable throwable) {
        ((CloseableStoreEventSink) eventSink).closeOnFailure();
    }

    @Override // org.ehcache.core.events.StoreEventDispatcher
    public void reset(StoreEventSink<K, V> eventSink) {
        ((CloseableStoreEventSink) eventSink).reset();
    }
}
