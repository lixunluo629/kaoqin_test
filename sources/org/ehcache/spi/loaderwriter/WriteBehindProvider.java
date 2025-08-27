package org.ehcache.spi.loaderwriter;

import org.ehcache.spi.service.Service;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/loaderwriter/WriteBehindProvider.class */
public interface WriteBehindProvider extends Service {
    <K, V> CacheLoaderWriter<K, V> createWriteBehindLoaderWriter(CacheLoaderWriter<K, V> cacheLoaderWriter, WriteBehindConfiguration writeBehindConfiguration);

    void releaseWriteBehindLoaderWriter(CacheLoaderWriter<?, ?> cacheLoaderWriter);
}
