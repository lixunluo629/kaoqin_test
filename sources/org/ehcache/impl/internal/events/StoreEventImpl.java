package org.ehcache.impl.internal.events;

import org.ehcache.core.spi.store.events.StoreEvent;
import org.ehcache.event.EventType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/StoreEventImpl.class */
public class StoreEventImpl<K, V> implements StoreEvent<K, V> {
    private final EventType type;
    private final K key;
    private final V oldValue;
    private final V newValue;

    public StoreEventImpl(EventType type, K key, V oldValue, V newValue) {
        this.type = type;
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override // org.ehcache.core.spi.store.events.StoreEvent
    public EventType getType() {
        return this.type;
    }

    @Override // org.ehcache.core.spi.store.events.StoreEvent
    public K getKey() {
        return this.key;
    }

    @Override // org.ehcache.core.spi.store.events.StoreEvent
    public V getNewValue() {
        return this.newValue;
    }

    @Override // org.ehcache.core.spi.store.events.StoreEvent
    public V getOldValue() {
        return this.oldValue;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoreEventImpl<?, ?> that = (StoreEventImpl) o;
        if (this.type != that.type || !this.key.equals(that.key)) {
            return false;
        }
        if (this.oldValue != null) {
            if (!this.oldValue.equals(that.oldValue)) {
                return false;
            }
        } else if (that.oldValue != null) {
            return false;
        }
        return this.newValue != null ? this.newValue.equals(that.newValue) : that.newValue == null;
    }

    public int hashCode() {
        int result = this.type.hashCode();
        return (31 * ((31 * ((31 * result) + this.key.hashCode())) + (this.oldValue != null ? this.oldValue.hashCode() : 0))) + (this.newValue != null ? this.newValue.hashCode() : 0);
    }

    public String toString() {
        return String.format("Event of type %s for key %s", this.type, this.key);
    }
}
