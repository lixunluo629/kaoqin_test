package org.ehcache.jsr107;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.cache.Cache;
import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryExpiredListener;
import javax.cache.event.CacheEntryListener;
import javax.cache.event.CacheEntryRemovedListener;
import javax.cache.event.CacheEntryUpdatedListener;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventType;
import org.ehcache.jsr107.Eh107CacheEntryEvent;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/EventListenerAdaptors.class */
class EventListenerAdaptors {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/EventListenerAdaptors$EventListenerAdaptor.class */
    static abstract class EventListenerAdaptor<K, V> implements CacheEventListener<K, V> {
        final CacheEntryEventFilter<K, V> filter;
        final Cache<K, V> source;
        final boolean requestsOld;

        abstract EventType getEhcacheEventType();

        EventListenerAdaptor(Cache<K, V> source, CacheEntryEventFilter<K, V> filter, boolean requestsOld) {
            this.source = source;
            this.filter = filter;
            this.requestsOld = requestsOld;
        }
    }

    static <K, V> List<EventListenerAdaptor<K, V>> ehListenersFor(CacheEntryListener<? super K, ? super V> listener, CacheEntryEventFilter<? super K, ? super V> filter, Cache<K, V> source, boolean requestsOld) {
        List<EventListenerAdaptor<K, V>> rv = new ArrayList<>();
        if (listener instanceof CacheEntryUpdatedListener) {
            rv.add(new UpdatedAdaptor<>(source, (CacheEntryUpdatedListener) listener, filter, requestsOld));
        }
        if (listener instanceof CacheEntryCreatedListener) {
            rv.add(new CreatedAdaptor<>(source, (CacheEntryCreatedListener) listener, filter, requestsOld));
        }
        if (listener instanceof CacheEntryRemovedListener) {
            rv.add(new RemovedAdaptor<>(source, (CacheEntryRemovedListener) listener, filter, requestsOld));
        }
        if (listener instanceof CacheEntryExpiredListener) {
            rv.add(new ExpiredAdaptor<>(source, (CacheEntryExpiredListener) listener, filter, requestsOld));
        }
        return rv;
    }

    private EventListenerAdaptors() {
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/EventListenerAdaptors$UpdatedAdaptor.class */
    static class UpdatedAdaptor<K, V> extends EventListenerAdaptor<K, V> {
        private final CacheEntryUpdatedListener<K, V> listener;

        UpdatedAdaptor(Cache<K, V> source, CacheEntryUpdatedListener<K, V> listener, CacheEntryEventFilter<K, V> filter, boolean requestsOld) {
            super(source, filter, requestsOld);
            this.listener = listener;
        }

        @Override // org.ehcache.jsr107.EventListenerAdaptors.EventListenerAdaptor
        EventType getEhcacheEventType() {
            return EventType.UPDATED;
        }

        @Override // org.ehcache.event.CacheEventListener
        public void onEvent(CacheEvent<? extends K, ? extends V> ehEvent) {
            Eh107CacheEntryEvent<K, V> event = new Eh107CacheEntryEvent.NormalEvent<>(this.source, javax.cache.event.EventType.UPDATED, ehEvent, this.requestsOld);
            if (this.filter.evaluate(event)) {
                Set<?> events = Collections.singleton(event);
                this.listener.onUpdated(events);
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/EventListenerAdaptors$RemovedAdaptor.class */
    static class RemovedAdaptor<K, V> extends EventListenerAdaptor<K, V> {
        private final CacheEntryRemovedListener<K, V> listener;

        RemovedAdaptor(Cache<K, V> source, CacheEntryRemovedListener<K, V> listener, CacheEntryEventFilter<K, V> filter, boolean requestsOld) {
            super(source, filter, requestsOld);
            this.listener = listener;
        }

        @Override // org.ehcache.jsr107.EventListenerAdaptors.EventListenerAdaptor
        EventType getEhcacheEventType() {
            return EventType.REMOVED;
        }

        @Override // org.ehcache.event.CacheEventListener
        public void onEvent(CacheEvent<? extends K, ? extends V> ehEvent) {
            Eh107CacheEntryEvent<K, V> event = new Eh107CacheEntryEvent.RemovingEvent<>(this.source, javax.cache.event.EventType.REMOVED, ehEvent, this.requestsOld);
            if (this.filter.evaluate(event)) {
                Set<?> events = Collections.singleton(event);
                this.listener.onRemoved(events);
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/EventListenerAdaptors$ExpiredAdaptor.class */
    static class ExpiredAdaptor<K, V> extends EventListenerAdaptor<K, V> {
        private final CacheEntryExpiredListener<K, V> listener;

        ExpiredAdaptor(Cache<K, V> source, CacheEntryExpiredListener<K, V> listener, CacheEntryEventFilter<K, V> filter, boolean requestsOld) {
            super(source, filter, requestsOld);
            this.listener = listener;
        }

        @Override // org.ehcache.jsr107.EventListenerAdaptors.EventListenerAdaptor
        EventType getEhcacheEventType() {
            return EventType.EXPIRED;
        }

        @Override // org.ehcache.event.CacheEventListener
        public void onEvent(CacheEvent<? extends K, ? extends V> ehEvent) {
            Eh107CacheEntryEvent<K, V> event = new Eh107CacheEntryEvent.RemovingEvent<>(this.source, javax.cache.event.EventType.EXPIRED, ehEvent, this.requestsOld);
            if (this.filter.evaluate(event)) {
                Set<?> events = Collections.singleton(event);
                this.listener.onExpired(events);
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/EventListenerAdaptors$CreatedAdaptor.class */
    static class CreatedAdaptor<K, V> extends EventListenerAdaptor<K, V> {
        private final CacheEntryCreatedListener<K, V> listener;

        CreatedAdaptor(Cache<K, V> source, CacheEntryCreatedListener<K, V> listener, CacheEntryEventFilter<K, V> filter, boolean requestsOld) {
            super(source, filter, requestsOld);
            this.listener = listener;
        }

        @Override // org.ehcache.jsr107.EventListenerAdaptors.EventListenerAdaptor
        EventType getEhcacheEventType() {
            return EventType.CREATED;
        }

        @Override // org.ehcache.event.CacheEventListener
        public void onEvent(CacheEvent<? extends K, ? extends V> ehEvent) {
            Eh107CacheEntryEvent<K, V> event = new Eh107CacheEntryEvent.NormalEvent<>(this.source, javax.cache.event.EventType.CREATED, ehEvent, this.requestsOld);
            if (this.filter.evaluate(event)) {
                Set<?> events = Collections.singleton(event);
                this.listener.onCreated(events);
            }
        }
    }
}
