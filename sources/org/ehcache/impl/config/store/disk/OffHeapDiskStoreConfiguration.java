package org.ehcache.impl.config.store.disk;

import org.ehcache.impl.internal.store.disk.OffHeapDiskStore;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/store/disk/OffHeapDiskStoreConfiguration.class */
public class OffHeapDiskStoreConfiguration implements ServiceConfiguration<OffHeapDiskStore.Provider> {
    private final String threadPoolAlias;
    private final int writerConcurrency;

    public OffHeapDiskStoreConfiguration(String threadPoolAlias, int writerConcurrency) {
        this.threadPoolAlias = threadPoolAlias;
        this.writerConcurrency = writerConcurrency;
    }

    public String getThreadPoolAlias() {
        return this.threadPoolAlias;
    }

    public int getWriterConcurrency() {
        return this.writerConcurrency;
    }

    @Override // org.ehcache.spi.service.ServiceConfiguration
    public Class<OffHeapDiskStore.Provider> getServiceType() {
        return OffHeapDiskStore.Provider.class;
    }
}
