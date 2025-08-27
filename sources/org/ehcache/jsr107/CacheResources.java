package org.ehcache.jsr107;

import java.io.Closeable;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.cache.CacheException;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import org.ehcache.jsr107.internal.Jsr107CacheLoaderWriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/CacheResources.class */
class CacheResources<K, V> {
    private final Eh107Expiry<K, V> expiryPolicy;
    private final Jsr107CacheLoaderWriter<? super K, V> cacheLoaderWriter;
    private final Map<CacheEntryListenerConfiguration<K, V>, ListenerResources<K, V>> listenerResources;
    private final AtomicBoolean closed;
    private final String cacheName;

    CacheResources(String cacheName, Jsr107CacheLoaderWriter<? super K, V> cacheLoaderWriter, Eh107Expiry<K, V> expiry, Map<CacheEntryListenerConfiguration<K, V>, ListenerResources<K, V>> listenerResources) {
        this.listenerResources = new ConcurrentHashMap();
        this.closed = new AtomicBoolean();
        this.cacheName = cacheName;
        this.cacheLoaderWriter = cacheLoaderWriter;
        this.expiryPolicy = expiry;
        this.listenerResources.putAll(listenerResources);
    }

    CacheResources(String cacheName, Jsr107CacheLoaderWriter<? super K, V> cacheLoaderWriter, Eh107Expiry<K, V> expiry) {
        this(cacheName, cacheLoaderWriter, expiry, new ConcurrentHashMap());
    }

    Eh107Expiry<K, V> getExpiryPolicy() {
        return this.expiryPolicy;
    }

    Jsr107CacheLoaderWriter<? super K, V> getCacheLoaderWriter() {
        return this.cacheLoaderWriter;
    }

    Map<CacheEntryListenerConfiguration<K, V>, ListenerResources<K, V>> getListenerResources() {
        return Collections.unmodifiableMap(this.listenerResources);
    }

    synchronized ListenerResources<K, V> registerCacheEntryListener(CacheEntryListenerConfiguration<K, V> listenerConfig) throws CacheException {
        checkClosed();
        if (this.listenerResources.containsKey(listenerConfig)) {
            throw new IllegalArgumentException("listener config already registered");
        }
        MultiCacheException mce = new MultiCacheException();
        ListenerResources<K, V> rv = ListenerResources.createListenerResources(listenerConfig, mce);
        mce.throwIfNotEmpty();
        this.listenerResources.put(listenerConfig, rv);
        return rv;
    }

    private void checkClosed() {
        if (this.closed.get()) {
            throw new IllegalStateException("cache resources closed for cache [" + this.cacheName + "]");
        }
    }

    synchronized ListenerResources<K, V> deregisterCacheEntryListener(CacheEntryListenerConfiguration<K, V> listenerConfig) throws CacheException {
        checkClosed();
        ListenerResources<K, V> resources = this.listenerResources.remove(listenerConfig);
        if (resources == null) {
            return null;
        }
        MultiCacheException mce = new MultiCacheException();
        close(resources, mce);
        mce.throwIfNotEmpty();
        return resources;
    }

    synchronized void closeResources(MultiCacheException mce) {
        if (this.closed.compareAndSet(false, true)) {
            close(this.expiryPolicy, mce);
            close(this.cacheLoaderWriter, mce);
            for (ListenerResources<K, V> lr : this.listenerResources.values()) {
                close(lr, mce);
            }
        }
    }

    boolean isClosed() {
        return this.closed.get();
    }

    static void close(Object obj, MultiCacheException mce) {
        if (obj instanceof Closeable) {
            try {
                ((Closeable) obj).close();
            } catch (Throwable t) {
                mce.addThrowable(t);
            }
        }
    }
}
