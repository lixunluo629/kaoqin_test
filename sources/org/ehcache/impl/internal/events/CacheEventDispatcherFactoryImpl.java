package org.ehcache.impl.internal.events;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import org.ehcache.core.events.CacheEventDispatcher;
import org.ehcache.core.events.CacheEventDispatcherFactory;
import org.ehcache.core.spi.service.ExecutionService;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.core.spi.store.Store;
import org.ehcache.impl.config.event.CacheEventDispatcherFactoryConfiguration;
import org.ehcache.impl.config.event.DefaultCacheEventDispatcherConfiguration;
import org.ehcache.impl.events.CacheEventDispatcherImpl;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceDependencies;
import org.ehcache.spi.service.ServiceProvider;

@ServiceDependencies({ExecutionService.class})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/CacheEventDispatcherFactoryImpl.class */
public class CacheEventDispatcherFactoryImpl implements CacheEventDispatcherFactory {
    private final String defaultThreadPoolAlias;
    private volatile ExecutionService executionService;

    public CacheEventDispatcherFactoryImpl() {
        this.defaultThreadPoolAlias = null;
    }

    public CacheEventDispatcherFactoryImpl(CacheEventDispatcherFactoryConfiguration configuration) {
        this.defaultThreadPoolAlias = configuration.getThreadPoolAlias();
    }

    @Override // org.ehcache.spi.service.Service
    public void start(ServiceProvider<Service> serviceProvider) {
        this.executionService = (ExecutionService) serviceProvider.getService(ExecutionService.class);
    }

    @Override // org.ehcache.spi.service.Service
    public void stop() {
    }

    @Override // org.ehcache.core.events.CacheEventDispatcherFactory
    public <K, V> CacheEventDispatcher<K, V> createCacheEventDispatcher(Store<K, V> store, ServiceConfiguration<?>... serviceConfigs) throws IllegalArgumentException {
        String threadPoolAlias = this.defaultThreadPoolAlias;
        DefaultCacheEventDispatcherConfiguration config = (DefaultCacheEventDispatcherConfiguration) ServiceUtils.findSingletonAmongst(DefaultCacheEventDispatcherConfiguration.class, serviceConfigs);
        if (config != null) {
            threadPoolAlias = config.getThreadPoolAlias();
        }
        ExecutorService orderedExecutor = this.executionService.getOrderedExecutor(threadPoolAlias, new LinkedBlockingQueue());
        ExecutorService unOrderedExecutor = this.executionService.getUnorderedExecutor(threadPoolAlias, new LinkedBlockingQueue());
        return new CacheEventDispatcherImpl(unOrderedExecutor, orderedExecutor);
    }

    @Override // org.ehcache.core.events.CacheEventDispatcherFactory
    public <K, V> void releaseCacheEventDispatcher(CacheEventDispatcher<K, V> eventDispatcher) {
        if (eventDispatcher != null) {
            eventDispatcher.shutdown();
        }
    }
}
