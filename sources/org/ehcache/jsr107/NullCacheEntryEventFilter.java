package org.ehcache.jsr107;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/NullCacheEntryEventFilter.class */
class NullCacheEntryEventFilter<K, V> implements CacheEntryEventFilter<K, V> {
    public static final CacheEntryEventFilter<?, ?> INSTANCE = new NullCacheEntryEventFilter();

    NullCacheEntryEventFilter() {
    }

    public boolean evaluate(CacheEntryEvent<? extends K, ? extends V> event) throws CacheEntryListenerException {
        return true;
    }
}
