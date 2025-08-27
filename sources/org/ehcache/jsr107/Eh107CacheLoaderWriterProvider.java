package org.ehcache.jsr107;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.impl.internal.spi.loaderwriter.DefaultCacheLoaderWriterProvider;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CacheLoaderWriterProvider.class */
class Eh107CacheLoaderWriterProvider extends DefaultCacheLoaderWriterProvider {
    private final ConcurrentMap<String, CacheLoaderWriter<?, ?>> cacheLoaderWriters;

    public Eh107CacheLoaderWriterProvider() {
        super(null);
        this.cacheLoaderWriters = new ConcurrentHashMap();
    }

    @Override // org.ehcache.impl.internal.spi.loaderwriter.DefaultCacheLoaderWriterProvider, org.ehcache.spi.loaderwriter.CacheLoaderWriterProvider
    public <K, V> CacheLoaderWriter<? super K, V> createCacheLoaderWriter(String alias, CacheConfiguration<K, V> cacheConfiguration) {
        CacheLoaderWriter<? super K, V> cacheLoaderWriter = (CacheLoaderWriter) this.cacheLoaderWriters.remove(alias);
        if (cacheLoaderWriter == null) {
            return super.createCacheLoaderWriter(alias, cacheConfiguration);
        }
        return cacheLoaderWriter;
    }

    @Override // org.ehcache.impl.internal.spi.loaderwriter.DefaultCacheLoaderWriterProvider, org.ehcache.spi.loaderwriter.CacheLoaderWriterProvider
    public void releaseCacheLoaderWriter(CacheLoaderWriter<?, ?> cacheLoaderWriter) {
    }

    <K, V> void registerJsr107Loader(String alias, CacheLoaderWriter<K, V> cacheLoaderWriter) {
        CacheLoaderWriter<?, ?> prev = this.cacheLoaderWriters.putIfAbsent(alias, cacheLoaderWriter);
        if (prev != null) {
            throw new IllegalStateException("loader already registered for [" + alias + "]");
        }
    }
}
