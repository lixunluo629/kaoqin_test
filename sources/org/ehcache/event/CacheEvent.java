package org.ehcache.event;

import org.ehcache.Cache;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/event/CacheEvent.class */
public interface CacheEvent<K, V> {
    EventType getType();

    K getKey();

    V getNewValue();

    V getOldValue();

    @Deprecated
    Cache<K, V> getSource();
}
