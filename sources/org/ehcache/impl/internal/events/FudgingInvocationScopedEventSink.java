package org.ehcache.impl.internal.events;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import org.ehcache.ValueSupplier;
import org.ehcache.core.spi.store.events.StoreEventFilter;
import org.ehcache.core.spi.store.events.StoreEventListener;
import org.ehcache.event.EventType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/FudgingInvocationScopedEventSink.class */
class FudgingInvocationScopedEventSink<K, V> extends InvocationScopedEventSink<K, V> {
    FudgingInvocationScopedEventSink(Set<StoreEventFilter<K, V>> filters, boolean ordered, BlockingQueue<FireableStoreEventHolder<K, V>>[] orderedQueues, Set<StoreEventListener<K, V>> listeners) {
        super(filters, ordered, orderedQueues, listeners);
    }

    @Override // org.ehcache.impl.internal.events.InvocationScopedEventSink, org.ehcache.core.events.StoreEventSink
    public void evicted(K key, ValueSupplier<V> value) throws InterruptedException {
        V eventFudgingValue = handleEvictionPostWriteOnSameKey(key);
        super.evicted(key, value);
        if (eventFudgingValue != null) {
            created(key, eventFudgingValue);
        }
    }

    private V handleEvictionPostWriteOnSameKey(K key) {
        Iterator<FireableStoreEventHolder<K, V>> iterator = getEvents().descendingIterator();
        while (iterator.hasNext()) {
            FireableStoreEventHolder<K, V> eventHolder = iterator.next();
            if (eventHolder.getEvent().getType() != EventType.EVICTED) {
                if (eventHolder.getEvent().getKey().equals(key)) {
                    switch (eventHolder.getEvent().getType()) {
                        case UPDATED:
                            eventHolder.markFailed();
                            return eventHolder.getEvent().getNewValue();
                        case CREATED:
                            eventHolder.markFailed();
                            if (iterator.hasNext()) {
                                FireableStoreEventHolder<K, V> next = iterator.next();
                                if (next.getEvent().getType() == EventType.EXPIRED && next.getEvent().getKey().equals(key)) {
                                    next.markFailed();
                                }
                            }
                            return eventHolder.getEvent().getNewValue();
                    }
                }
                return null;
            }
        }
        return null;
    }
}
