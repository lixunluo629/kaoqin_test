package org.ehcache.core.events;

import org.ehcache.Cache;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.EventType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheEvents.class */
public final class CacheEvents {
    private CacheEvents() {
    }

    public static <K, V> CacheEvent<K, V> expiry(K expiredKey, V expiredValue, Cache<K, V> source) {
        return new ExpiryEvent(expiredKey, expiredValue, source);
    }

    public static <K, V> CacheEvent<K, V> eviction(K evictedKey, V evictedValue, Cache<K, V> source) {
        return new EvictionEvent(evictedKey, evictedValue, source);
    }

    public static <K, V> CacheEvent<K, V> creation(K newKey, V newValue, Cache<K, V> source) {
        return new CreationEvent(newKey, newValue, source);
    }

    public static <K, V> CacheEvent<K, V> removal(K removedKey, V removedValue, Cache<K, V> source) {
        return new RemovalEvent(removedKey, removedValue, source);
    }

    public static <K, V> CacheEvent<K, V> update(K key, V oldValue, V newValue, Cache<K, V> source) {
        return new UpdateEvent(key, oldValue, newValue, source);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheEvents$BaseCacheEvent.class */
    private static abstract class BaseCacheEvent<K, V> implements CacheEvent<K, V> {
        final K key;
        final Cache<K, V> src;

        protected BaseCacheEvent(K key, Cache<K, V> from) {
            this.key = key;
            this.src = from;
        }

        @Override // org.ehcache.event.CacheEvent
        public K getKey() {
            return this.key;
        }

        @Override // org.ehcache.event.CacheEvent
        @Deprecated
        public Cache<K, V> getSource() {
            return this.src;
        }

        public String toString() {
            return getType() + " on " + this.src + " key,oldValue,newValue='" + getKey() + "','" + getOldValue() + "','" + getNewValue() + "'";
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheEvents$ExpiryEvent.class */
    private static final class ExpiryEvent<K, V> extends BaseCacheEvent<K, V> {
        final V expiredValue;

        ExpiryEvent(K expiredKey, V expiredValue, Cache<K, V> src) {
            super(expiredKey, src);
            this.expiredValue = expiredValue;
        }

        @Override // org.ehcache.event.CacheEvent
        public EventType getType() {
            return EventType.EXPIRED;
        }

        @Override // org.ehcache.event.CacheEvent
        public V getNewValue() {
            return null;
        }

        @Override // org.ehcache.event.CacheEvent
        public V getOldValue() {
            return this.expiredValue;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheEvents$EvictionEvent.class */
    private static final class EvictionEvent<K, V> extends BaseCacheEvent<K, V> {
        final V evictedValue;

        EvictionEvent(K evictedKey, V evictedValue, Cache<K, V> src) {
            super(evictedKey, src);
            this.evictedValue = evictedValue;
        }

        @Override // org.ehcache.event.CacheEvent
        public EventType getType() {
            return EventType.EVICTED;
        }

        @Override // org.ehcache.event.CacheEvent
        public V getNewValue() {
            return null;
        }

        @Override // org.ehcache.event.CacheEvent
        public V getOldValue() {
            return this.evictedValue;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheEvents$CreationEvent.class */
    private static final class CreationEvent<K, V> extends BaseCacheEvent<K, V> {
        final V newValue;

        CreationEvent(K newKey, V newValue, Cache<K, V> src) {
            super(newKey, src);
            this.newValue = newValue;
        }

        @Override // org.ehcache.event.CacheEvent
        public EventType getType() {
            return EventType.CREATED;
        }

        @Override // org.ehcache.event.CacheEvent
        public V getNewValue() {
            return this.newValue;
        }

        @Override // org.ehcache.event.CacheEvent
        public V getOldValue() {
            return null;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheEvents$RemovalEvent.class */
    private static final class RemovalEvent<K, V> extends BaseCacheEvent<K, V> {
        final V removedValue;

        RemovalEvent(K removedKey, V removedValue, Cache<K, V> src) {
            super(removedKey, src);
            this.removedValue = removedValue;
        }

        @Override // org.ehcache.event.CacheEvent
        public EventType getType() {
            return EventType.REMOVED;
        }

        @Override // org.ehcache.event.CacheEvent
        public V getNewValue() {
            return null;
        }

        @Override // org.ehcache.event.CacheEvent
        public V getOldValue() {
            return this.removedValue;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheEvents$UpdateEvent.class */
    private static final class UpdateEvent<K, V> extends BaseCacheEvent<K, V> {
        final V oldValue;
        final V newValue;

        UpdateEvent(K key, V oldValue, V newValue, Cache<K, V> src) {
            super(key, src);
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        @Override // org.ehcache.event.CacheEvent
        public EventType getType() {
            return EventType.UPDATED;
        }

        @Override // org.ehcache.event.CacheEvent
        public V getNewValue() {
            return this.newValue;
        }

        @Override // org.ehcache.event.CacheEvent
        public V getOldValue() {
            return this.oldValue;
        }
    }
}
