package org.ehcache.impl.internal.loaderwriter.writebehind;

import org.ehcache.core.spi.service.ExecutionService;
import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.config.loaderwriter.writebehind.WriteBehindProviderConfiguration;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.WriteBehindConfiguration;
import org.ehcache.spi.loaderwriter.WriteBehindProvider;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.ehcache.spi.service.ServiceDependencies;
import org.ehcache.spi.service.ServiceProvider;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/WriteBehindProviderFactory.class */
public class WriteBehindProviderFactory implements ServiceFactory<WriteBehindProvider> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<WriteBehindProvider>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public WriteBehindProvider create(ServiceCreationConfiguration<WriteBehindProvider> configuration) {
        if (configuration == null) {
            return new Provider();
        }
        if (configuration instanceof WriteBehindProviderConfiguration) {
            return new Provider(((WriteBehindProviderConfiguration) configuration).getThreadPoolAlias());
        }
        throw new IllegalArgumentException("WriteBehind configuration must not be provided at CacheManager level");
    }

    @ServiceDependencies({ExecutionService.class})
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/WriteBehindProviderFactory$Provider.class */
    public static class Provider implements WriteBehindProvider {
        private final String threadPoolAlias;
        private volatile ExecutionService executionService;

        protected Provider() {
            this(null);
        }

        protected Provider(String threadPoolAlias) {
            this.threadPoolAlias = threadPoolAlias;
        }

        @Override // org.ehcache.spi.service.Service
        public void stop() {
        }

        @Override // org.ehcache.spi.service.Service
        public void start(ServiceProvider<Service> serviceProvider) {
            this.executionService = (ExecutionService) serviceProvider.getService(ExecutionService.class);
        }

        @Override // org.ehcache.spi.loaderwriter.WriteBehindProvider
        public <K, V> WriteBehind<K, V> createWriteBehindLoaderWriter(CacheLoaderWriter<K, V> cacheLoaderWriter, WriteBehindConfiguration configuration) {
            if (cacheLoaderWriter == null) {
                throw new NullPointerException("WriteBehind requires a non null CacheLoaderWriter.");
            }
            return new StripedWriteBehind(this.executionService, this.threadPoolAlias, configuration, cacheLoaderWriter);
        }

        @Override // org.ehcache.spi.loaderwriter.WriteBehindProvider
        public void releaseWriteBehindLoaderWriter(CacheLoaderWriter<?, ?> cacheLoaderWriter) {
            if (cacheLoaderWriter != null) {
                ((WriteBehind) cacheLoaderWriter).stop();
            }
        }
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<WriteBehindProvider> getServiceType() {
        return WriteBehindProvider.class;
    }
}
