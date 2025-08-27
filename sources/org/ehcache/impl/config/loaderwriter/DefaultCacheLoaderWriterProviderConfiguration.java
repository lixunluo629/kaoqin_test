package org.ehcache.impl.config.loaderwriter;

import org.ehcache.impl.internal.classes.ClassInstanceProviderConfiguration;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheLoaderWriterProvider;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/loaderwriter/DefaultCacheLoaderWriterProviderConfiguration.class */
public class DefaultCacheLoaderWriterProviderConfiguration extends ClassInstanceProviderConfiguration<String, CacheLoaderWriter<?, ?>> implements ServiceCreationConfiguration<CacheLoaderWriterProvider> {
    @Override // org.ehcache.spi.service.ServiceCreationConfiguration
    public Class<CacheLoaderWriterProvider> getServiceType() {
        return CacheLoaderWriterProvider.class;
    }

    public DefaultCacheLoaderWriterProviderConfiguration addLoaderFor(String alias, Class<? extends CacheLoaderWriter<?, ?>> clazz, Object... arguments) {
        getDefaults().put(alias, new DefaultCacheLoaderWriterConfiguration(clazz, arguments));
        return this;
    }
}
