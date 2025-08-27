package org.ehcache.impl.internal.sizeof;

import org.ehcache.config.ResourceUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.core.spi.store.heap.SizeOfEngine;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineConfiguration;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceProvider;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/sizeof/DefaultSizeOfEngineProvider.class */
public class DefaultSizeOfEngineProvider implements SizeOfEngineProvider {
    private final long maxObjectGraphSize;
    private final long maxObjectSize;

    public DefaultSizeOfEngineProvider(long maxObjectGraphSize, long maxObjectSize) {
        this.maxObjectGraphSize = maxObjectGraphSize;
        this.maxObjectSize = maxObjectSize;
    }

    @Override // org.ehcache.spi.service.Service
    public void start(ServiceProvider<Service> serviceProvider) {
    }

    @Override // org.ehcache.spi.service.Service
    public void stop() {
    }

    @Override // org.ehcache.core.spi.store.heap.SizeOfEngineProvider
    public SizeOfEngine createSizeOfEngine(ResourceUnit resourceUnit, ServiceConfiguration<?>... serviceConfigs) {
        boolean isByteSized = resourceUnit instanceof MemoryUnit;
        if (!isByteSized) {
            return new NoopSizeOfEngine();
        }
        DefaultSizeOfEngineConfiguration config = (DefaultSizeOfEngineConfiguration) ServiceUtils.findSingletonAmongst(DefaultSizeOfEngineConfiguration.class, serviceConfigs);
        if (config != null) {
            long maxSize = config.getUnit().toBytes(config.getMaxObjectSize());
            return new DefaultSizeOfEngine(config.getMaxObjectGraphSize(), maxSize);
        }
        return new DefaultSizeOfEngine(this.maxObjectGraphSize, this.maxObjectSize);
    }
}
