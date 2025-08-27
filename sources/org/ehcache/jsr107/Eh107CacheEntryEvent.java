package org.ehcache.jsr107;

import javax.cache.Cache;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.EventType;
import org.ehcache.event.CacheEvent;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CacheEntryEvent.class */
abstract class Eh107CacheEntryEvent<K, V> extends CacheEntryEvent<K, V> {
    private static final long serialVersionUID = 8460535666272347345L;
    private final CacheEvent<? extends K, ? extends V> ehEvent;
    private final boolean hasOldValue;

    public abstract V getValue();

    Eh107CacheEntryEvent(Cache<K, V> source, EventType eventType, CacheEvent<? extends K, ? extends V> ehEvent, boolean hasOldValue) {
        super(source, eventType);
        this.ehEvent = ehEvent;
        this.hasOldValue = hasOldValue;
    }

    public K getKey() {
        return this.ehEvent.getKey();
    }

    public <T> T unwrap(Class<T> cls) {
        return (T) Unwrap.unwrap(cls, this, this.ehEvent);
    }

    public V getOldValue() {
        return this.ehEvent.getOldValue();
    }

    public boolean isOldValueAvailable() {
        return this.hasOldValue;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CacheEntryEvent$NormalEvent.class */
    static class NormalEvent<K, V> extends Eh107CacheEntryEvent<K, V> {
        public NormalEvent(Cache<K, V> source, EventType eventType, CacheEvent<? extends K, ? extends V> ehEvent, boolean hasOldValue) {
            super(source, eventType, ehEvent, hasOldValue);
        }

        @Override // org.ehcache.jsr107.Eh107CacheEntryEvent
        public V getValue() {
            return (V) ((Eh107CacheEntryEvent) this).ehEvent.getNewValue();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CacheEntryEvent$RemovingEvent.class */
    static class RemovingEvent<K, V> extends Eh107CacheEntryEvent<K, V> {
        public RemovingEvent(Cache<K, V> source, EventType eventType, CacheEvent<? extends K, ? extends V> ehEvent, boolean hasOldValue) {
            super(source, eventType, ehEvent, hasOldValue);
        }

        @Override // org.ehcache.jsr107.Eh107CacheEntryEvent
        public V getValue() {
            return (V) ((Eh107CacheEntryEvent) this).ehEvent.getOldValue();
        }
    }
}
