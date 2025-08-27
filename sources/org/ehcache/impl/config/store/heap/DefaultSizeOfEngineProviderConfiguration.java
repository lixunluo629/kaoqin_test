package org.ehcache.impl.config.store.heap;

import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/store/heap/DefaultSizeOfEngineProviderConfiguration.class */
public class DefaultSizeOfEngineProviderConfiguration implements ServiceCreationConfiguration<SizeOfEngineProvider> {
    private final long objectGraphSize;
    private final long maxObjectSize;
    private final MemoryUnit unit;

    public DefaultSizeOfEngineProviderConfiguration(long size, MemoryUnit unit, long objectGraphSize) {
        if (size <= 0 || objectGraphSize <= 0) {
            throw new IllegalArgumentException("SizeOfEngine cannot take non-positive arguments.");
        }
        this.objectGraphSize = objectGraphSize;
        this.maxObjectSize = size;
        this.unit = unit;
    }

    @Override // org.ehcache.spi.service.ServiceCreationConfiguration
    public Class<SizeOfEngineProvider> getServiceType() {
        return SizeOfEngineProvider.class;
    }

    public long getMaxObjectGraphSize() {
        return this.objectGraphSize;
    }

    public long getMaxObjectSize() {
        return this.maxObjectSize;
    }

    public MemoryUnit getUnit() {
        return this.unit;
    }
}
