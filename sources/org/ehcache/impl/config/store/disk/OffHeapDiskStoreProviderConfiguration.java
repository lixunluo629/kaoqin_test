package org.ehcache.impl.config.store.disk;

import org.ehcache.impl.internal.store.disk.OffHeapDiskStore;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/store/disk/OffHeapDiskStoreProviderConfiguration.class */
public class OffHeapDiskStoreProviderConfiguration implements ServiceCreationConfiguration<OffHeapDiskStore.Provider> {
    private final String threadPoolAlias;

    public OffHeapDiskStoreProviderConfiguration(String threadPoolAlias) {
        this.threadPoolAlias = threadPoolAlias;
    }

    public String getThreadPoolAlias() {
        return this.threadPoolAlias;
    }

    @Override // org.ehcache.spi.service.ServiceCreationConfiguration
    public Class<OffHeapDiskStore.Provider> getServiceType() {
        return OffHeapDiskStore.Provider.class;
    }
}
