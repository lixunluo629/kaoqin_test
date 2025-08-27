package org.ehcache.impl.events;

import org.ehcache.core.internal.events.EventListenerWrapper;
import org.ehcache.event.CacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/events/EventDispatchTask.class */
class EventDispatchTask<K, V> implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) EventDispatchTask.class);
    private final CacheEvent<K, V> cacheEvent;
    private final Iterable<EventListenerWrapper<K, V>> listenerWrappers;

    EventDispatchTask(CacheEvent<K, V> cacheEvent, Iterable<EventListenerWrapper<K, V>> listener) {
        if (cacheEvent == null) {
            throw new NullPointerException("cache event cannot be null");
        }
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }
        this.cacheEvent = cacheEvent;
        this.listenerWrappers = listener;
    }

    @Override // java.lang.Runnable
    public void run() {
        for (EventListenerWrapper<K, V> listenerWrapper : this.listenerWrappers) {
            if (listenerWrapper.isForEventType(this.cacheEvent.getType())) {
                try {
                    listenerWrapper.onEvent(this.cacheEvent);
                } catch (Exception e) {
                    LOGGER.warn(listenerWrapper.getListener() + " Failed to fire Event due to ", (Throwable) e);
                }
            }
        }
    }
}
