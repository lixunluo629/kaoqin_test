package org.ehcache.impl.internal.events;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import org.ehcache.ValueSupplier;
import org.ehcache.core.spi.store.events.StoreEventFilter;
import org.ehcache.core.spi.store.events.StoreEventListener;
import org.ehcache.event.EventType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/InvocationScopedEventSink.class */
class InvocationScopedEventSink<K, V> implements CloseableStoreEventSink<K, V> {
    private final Set<StoreEventFilter<K, V>> filters;
    private final boolean ordered;
    private final BlockingQueue<FireableStoreEventHolder<K, V>>[] orderedQueues;
    private final Set<StoreEventListener<K, V>> listeners;
    private final Deque<FireableStoreEventHolder<K, V>> events = new ArrayDeque(4);

    InvocationScopedEventSink(Set<StoreEventFilter<K, V>> filters, boolean ordered, BlockingQueue<FireableStoreEventHolder<K, V>>[] orderedQueues, Set<StoreEventListener<K, V>> listeners) {
        this.filters = filters;
        this.ordered = ordered;
        this.orderedQueues = orderedQueues;
        this.listeners = listeners;
    }

    @Override // org.ehcache.core.events.StoreEventSink
    public void removed(K key, ValueSupplier<V> value) throws InterruptedException {
        V removedValue = value.value();
        if (acceptEvent(EventType.REMOVED, key, removedValue, null)) {
            handleEvent(key, new FireableStoreEventHolder<>(StoreEvents.removeEvent(key, removedValue)));
        }
    }

    @Override // org.ehcache.core.events.StoreEventSink
    public void updated(K key, ValueSupplier<V> oldValue, V newValue) throws InterruptedException {
        V oldValueValue = oldValue.value();
        if (acceptEvent(EventType.UPDATED, key, oldValueValue, newValue)) {
            handleEvent(key, new FireableStoreEventHolder<>(StoreEvents.updateEvent(key, oldValueValue, newValue)));
        }
    }

    @Override // org.ehcache.core.events.StoreEventSink
    public void expired(K key, ValueSupplier<V> value) throws InterruptedException {
        V expired = value.value();
        if (acceptEvent(EventType.EXPIRED, key, expired, null)) {
            handleEvent(key, new FireableStoreEventHolder<>(StoreEvents.expireEvent(key, expired)));
        }
    }

    @Override // org.ehcache.core.events.StoreEventSink
    public void created(K key, V value) throws InterruptedException {
        if (acceptEvent(EventType.CREATED, key, null, value)) {
            handleEvent(key, new FireableStoreEventHolder<>(StoreEvents.createEvent(key, value)));
        }
    }

    @Override // org.ehcache.core.events.StoreEventSink
    public void evicted(K key, ValueSupplier<V> value) throws InterruptedException {
        V evicted = value.value();
        if (acceptEvent(EventType.EVICTED, key, evicted, null)) {
            handleEvent(key, new FireableStoreEventHolder<>(StoreEvents.evictEvent(key, evicted)));
        }
    }

    protected boolean acceptEvent(EventType type, K key, V oldValue, V newValue) {
        for (StoreEventFilter<K, V> filter : this.filters) {
            if (!filter.acceptEvent(type, key, oldValue, newValue)) {
                return false;
            }
        }
        return true;
    }

    @Override // org.ehcache.impl.internal.events.CloseableStoreEventSink
    public void close() {
        if (this.ordered) {
            fireOrdered(this.listeners, this.events);
            return;
        }
        for (FireableStoreEventHolder<K, V> fireableEvent : this.events) {
            for (StoreEventListener<K, V> listener : this.listeners) {
                fireableEvent.fireOn(listener);
            }
        }
    }

    @Override // org.ehcache.impl.internal.events.CloseableStoreEventSink
    public void closeOnFailure() {
        for (FireableStoreEventHolder<K, V> fireableEvent : this.events) {
            fireableEvent.markFailed();
        }
        close();
    }

    @Override // org.ehcache.impl.internal.events.CloseableStoreEventSink
    public void reset() {
        Iterator<FireableStoreEventHolder<K, V>> iterator = this.events.iterator();
        while (iterator.hasNext()) {
            FireableStoreEventHolder<K, V> next = iterator.next();
            if (this.ordered) {
                BlockingQueue<FireableStoreEventHolder<K, V>> orderedQueue = getOrderedQueue(next);
                orderedQueue.remove(next);
                fireWaiters(this.listeners, orderedQueue);
            }
            iterator.remove();
        }
    }

    protected Deque<FireableStoreEventHolder<K, V>> getEvents() {
        return this.events;
    }

    protected void handleEvent(K key, FireableStoreEventHolder<K, V> event) throws InterruptedException {
        this.events.add(event);
        if (this.ordered) {
            try {
                getOrderedQueue(event).put(event);
            } catch (InterruptedException e) {
                this.events.removeLast();
                Thread.currentThread().interrupt();
            }
        }
    }

    private BlockingQueue<FireableStoreEventHolder<K, V>> getOrderedQueue(FireableStoreEventHolder<K, V> event) {
        int i = Math.abs(event.eventKeyHash() % this.orderedQueues.length);
        return this.orderedQueues[i];
    }

    private void fireOrdered(Set<StoreEventListener<K, V>> listeners, Deque<FireableStoreEventHolder<K, V>> events) {
        for (FireableStoreEventHolder<K, V> fireableEvent : events) {
            fireableEvent.markFireable();
            BlockingQueue<FireableStoreEventHolder<K, V>> orderedQueue = getOrderedQueue(fireableEvent);
            FireableStoreEventHolder<K, V> head = orderedQueue.peek();
            if (head == fireableEvent) {
                if (head.markFired()) {
                    for (StoreEventListener<K, V> listener : listeners) {
                        head.fireOn(listener);
                    }
                    orderedQueue.poll();
                } else {
                    fireableEvent.waitTillFired();
                }
                fireWaiters(listeners, orderedQueue);
            } else {
                fireableEvent.waitTillFired();
            }
        }
    }

    private void fireWaiters(Set<StoreEventListener<K, V>> listeners, BlockingQueue<FireableStoreEventHolder<K, V>> orderedQueue) {
        while (true) {
            FireableStoreEventHolder<K, V> head = orderedQueue.peek();
            if (head != null && head.isFireable() && head.markFired()) {
                for (StoreEventListener<K, V> listener : listeners) {
                    head.fireOn(listener);
                }
                orderedQueue.poll();
            } else {
                return;
            }
        }
    }
}
