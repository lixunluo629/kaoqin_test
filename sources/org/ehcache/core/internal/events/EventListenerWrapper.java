package org.ehcache.core.internal.events;

import java.util.EnumSet;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.event.EventType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/events/EventListenerWrapper.class */
public final class EventListenerWrapper<K, V> implements CacheEventListener<K, V> {
    private final CacheEventListener<? super K, ? super V> listener;
    private final EventFiring firing;
    private final EventOrdering ordering;
    private final EnumSet<EventType> forEvents;

    public EventListenerWrapper(CacheEventListener<? super K, ? super V> listener) {
        this.listener = listener;
        this.firing = null;
        this.ordering = null;
        this.forEvents = null;
    }

    public EventListenerWrapper(CacheEventListener<? super K, ? super V> listener, EventFiring firing, EventOrdering ordering, EnumSet<EventType> forEvents) {
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }
        if (firing == null) {
            throw new NullPointerException("firing cannot be null");
        }
        if (ordering == null) {
            throw new NullPointerException("ordering cannot be null");
        }
        if (forEvents == null) {
            throw new NullPointerException("forEvents cannot be null");
        }
        if (forEvents.isEmpty()) {
            throw new IllegalArgumentException("forEvents cannot be empty");
        }
        this.listener = listener;
        this.firing = firing;
        this.ordering = ordering;
        this.forEvents = forEvents;
    }

    public int hashCode() {
        return this.listener.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof EventListenerWrapper)) {
            return false;
        }
        EventListenerWrapper l2 = (EventListenerWrapper) other;
        return this.listener.equals(l2.listener);
    }

    @Override // org.ehcache.event.CacheEventListener
    public void onEvent(CacheEvent<? extends K, ? extends V> event) {
        this.listener.onEvent(event);
    }

    public CacheEventListener getListener() {
        return this.listener;
    }

    public boolean isForEventType(EventType type) {
        return this.forEvents.contains(type);
    }

    public boolean isOrdered() {
        return this.ordering.isOrdered();
    }

    public EventFiring getFiringMode() {
        return this.firing;
    }
}
