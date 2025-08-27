package org.ehcache.impl.internal.spi.loaderwriter;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.impl.config.loaderwriter.DefaultCacheLoaderWriterConfiguration;
import org.ehcache.impl.config.loaderwriter.DefaultCacheLoaderWriterProviderConfiguration;
import org.ehcache.impl.internal.classes.ClassInstanceProvider;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheLoaderWriterProvider;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/spi/loaderwriter/DefaultCacheLoaderWriterProvider.class */
public class DefaultCacheLoaderWriterProvider extends ClassInstanceProvider<String, CacheLoaderWriter<?, ?>> implements CacheLoaderWriterProvider {
    public DefaultCacheLoaderWriterProvider(DefaultCacheLoaderWriterProviderConfiguration configuration) {
        super(configuration, DefaultCacheLoaderWriterConfiguration.class, true);
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriterProvider
    public <K, V> CacheLoaderWriter<? super K, V> createCacheLoaderWriter(String alias, CacheConfiguration<K, V> cacheConfiguration) {
        return (CacheLoaderWriter) newInstance((DefaultCacheLoaderWriterProvider) alias, (CacheConfiguration<?, ?>) cacheConfiguration);
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriterProvider
    public void releaseCacheLoaderWriter(CacheLoaderWriter<?, ?> cacheLoaderWriter) throws Exception {
        releaseInstance(cacheLoaderWriter);
    }
}
