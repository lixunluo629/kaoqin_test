package org.ehcache.impl.config.store.heap;

import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/store/heap/DefaultSizeOfEngineConfiguration.class */
public class DefaultSizeOfEngineConfiguration implements ServiceConfiguration<SizeOfEngineProvider> {
    public static final int DEFAULT_OBJECT_GRAPH_SIZE = 1000;
    public static final long DEFAULT_MAX_OBJECT_SIZE = Long.MAX_VALUE;
    public static final MemoryUnit DEFAULT_UNIT = MemoryUnit.B;
    private final long objectGraphSize;
    private final long maxObjectSize;
    private final MemoryUnit unit;

    public DefaultSizeOfEngineConfiguration(long size, MemoryUnit unit, long objectGraphSize) {
        if (size <= 0 || objectGraphSize <= 0) {
            throw new IllegalArgumentException("ObjectGraphSize/ObjectSize can only accept positive values.");
        }
        this.objectGraphSize = objectGraphSize;
        this.maxObjectSize = size;
        this.unit = unit;
    }

    @Override // org.ehcache.spi.service.ServiceConfiguration
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
