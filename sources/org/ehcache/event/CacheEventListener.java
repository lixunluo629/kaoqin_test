package org.ehcache.event;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/event/CacheEventListener.class */
public interface CacheEventListener<K, V> {
    void onEvent(CacheEvent<? extends K, ? extends V> cacheEvent);
}
