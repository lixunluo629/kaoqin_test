package org.ehcache.spi.loaderwriter;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.spi.service.Service;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/loaderwriter/CacheLoaderWriterProvider.class */
public interface CacheLoaderWriterProvider extends Service {
    <K, V> CacheLoaderWriter<? super K, V> createCacheLoaderWriter(String str, CacheConfiguration<K, V> cacheConfiguration);

    void releaseCacheLoaderWriter(CacheLoaderWriter<?, ?> cacheLoaderWriter) throws Exception;
}
