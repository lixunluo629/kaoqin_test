package org.ehcache.jsr107.internal;

import java.util.Map;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/internal/WrappedCacheLoaderWriter.class */
public class WrappedCacheLoaderWriter<K, V> implements Jsr107CacheLoaderWriter<K, V> {
    private final CacheLoaderWriter<K, V> delegate;

    public WrappedCacheLoaderWriter(CacheLoaderWriter<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override // org.ehcache.jsr107.internal.Jsr107CacheLoaderWriter
    public Map<K, V> loadAllAlways(Iterable<? extends K> keys) throws Exception {
        return this.delegate.loadAll(keys);
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public V load(K key) throws Exception {
        return this.delegate.load(key);
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
        return this.delegate.loadAll(keys);
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void write(K key, V value) throws Exception {
        this.delegate.write(key, value);
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void writeAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) throws Exception {
        this.delegate.writeAll(entries);
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void delete(K key) throws Exception {
        this.delegate.delete(key);
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void deleteAll(Iterable<? extends K> keys) throws Exception {
        this.delegate.deleteAll(keys);
    }
}
