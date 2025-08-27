package org.ehcache.jsr107;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListener;
import org.ehcache.jsr107.EventListenerAdaptors;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/ListenerResources.class */
class ListenerResources<K, V> implements Closeable {
    private final CacheEntryListener<? super K, ? super V> listener;
    private final CacheEntryEventFilter<? super K, ? super V> filter;
    private List<EventListenerAdaptors.EventListenerAdaptor<K, V>> ehListeners = null;

    /* JADX WARN: Multi-variable type inference failed */
    static <K, V> ListenerResources<K, V> createListenerResources(CacheEntryListenerConfiguration<K, V> listenerConfig, MultiCacheException multiCacheException) {
        CacheEntryEventFilter<?, ?> cacheEntryEventFilter;
        CacheEntryListener<? super K, ? super V> listener = (CacheEntryListener) listenerConfig.getCacheEntryListenerFactory().create();
        try {
            Factory<CacheEntryEventFilter<? super K, ? super V>> filterFactory = listenerConfig.getCacheEntryEventFilterFactory();
            if (filterFactory != null) {
                cacheEntryEventFilter = (CacheEntryEventFilter) listenerConfig.getCacheEntryEventFilterFactory().create();
            } else {
                cacheEntryEventFilter = NullCacheEntryEventFilter.INSTANCE;
            }
            try {
                return new ListenerResources<>(listener, cacheEntryEventFilter);
            } catch (Throwable t) {
                multiCacheException.addThrowable(t);
                CacheResources.close(cacheEntryEventFilter, multiCacheException);
                CacheResources.close(listener, multiCacheException);
                throw multiCacheException;
            }
        } catch (Throwable t2) {
            multiCacheException.addThrowable(t2);
            CacheResources.close(listener, multiCacheException);
            throw multiCacheException;
        }
    }

    ListenerResources(CacheEntryListener<? super K, ? super V> listener, CacheEntryEventFilter<? super K, ? super V> filter) {
        this.listener = listener;
        this.filter = filter;
    }

    CacheEntryEventFilter<? super K, ? super V> getFilter() {
        return this.filter;
    }

    CacheEntryListener<? super K, ? super V> getListener() {
        return this.listener;
    }

    synchronized List<EventListenerAdaptors.EventListenerAdaptor<K, V>> getEhcacheListeners(Cache<K, V> source, boolean requestsOld) {
        if (this.ehListeners == null) {
            this.ehListeners = EventListenerAdaptors.ehListenersFor(this.listener, this.filter, source, requestsOld);
        }
        return Collections.unmodifiableList(this.ehListeners);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws CacheException, IOException {
        MultiCacheException mce = new MultiCacheException();
        CacheResources.close(this.listener, mce);
        CacheResources.close(this.filter, mce);
        mce.throwIfNotEmpty();
    }
}
