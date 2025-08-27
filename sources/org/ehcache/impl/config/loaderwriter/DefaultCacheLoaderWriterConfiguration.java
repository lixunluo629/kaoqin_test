package org.ehcache.impl.config.loaderwriter;

import org.ehcache.impl.internal.classes.ClassInstanceConfiguration;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheLoaderWriterProvider;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/loaderwriter/DefaultCacheLoaderWriterConfiguration.class */
public class DefaultCacheLoaderWriterConfiguration extends ClassInstanceConfiguration<CacheLoaderWriter<?, ?>> implements ServiceConfiguration<CacheLoaderWriterProvider> {
    public DefaultCacheLoaderWriterConfiguration(Class<? extends CacheLoaderWriter<?, ?>> clazz, Object... arguments) {
        super(clazz, arguments);
    }

    public DefaultCacheLoaderWriterConfiguration(CacheLoaderWriter<?, ?> loaderWriter) {
        super(loaderWriter);
    }

    @Override // org.ehcache.spi.service.ServiceConfiguration
    public Class<CacheLoaderWriterProvider> getServiceType() {
        return CacheLoaderWriterProvider.class;
    }
}
