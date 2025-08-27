package org.ehcache.impl.events;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/events/CacheEventAdapter.class */
public abstract class CacheEventAdapter<K, V> implements CacheEventListener<K, V> {
    @Override // org.ehcache.event.CacheEventListener
    public final void onEvent(CacheEvent<? extends K, ? extends V> event) {
        switch (event.getType()) {
            case CREATED:
                onCreation(event.getKey(), event.getNewValue());
                return;
            case UPDATED:
                onUpdate(event.getKey(), event.getOldValue(), event.getNewValue());
                return;
            case REMOVED:
                onRemoval(event.getKey(), event.getOldValue());
                return;
            case EXPIRED:
                onExpiry(event.getKey(), event.getOldValue());
                return;
            case EVICTED:
                onEviction(event.getKey(), event.getOldValue());
                return;
            default:
                throw new AssertionError("Unsupported event type " + event.getType());
        }
    }

    protected void onEviction(K key, V evictedValue) {
    }

    protected void onExpiry(K key, V expiredValue) {
    }

    protected void onRemoval(K key, V removedValue) {
    }

    protected void onUpdate(K key, V oldValue, V newValue) {
    }

    protected void onCreation(K key, V newValue) {
    }
}
