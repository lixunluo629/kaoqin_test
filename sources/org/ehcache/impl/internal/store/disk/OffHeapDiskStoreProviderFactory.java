package org.ehcache.impl.internal.store.disk;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.config.store.disk.OffHeapDiskStoreProviderConfiguration;
import org.ehcache.impl.internal.store.disk.OffHeapDiskStore;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/disk/OffHeapDiskStoreProviderFactory.class */
public class OffHeapDiskStoreProviderFactory implements ServiceFactory<OffHeapDiskStore.Provider> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<OffHeapDiskStore.Provider>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public OffHeapDiskStore.Provider create(ServiceCreationConfiguration<OffHeapDiskStore.Provider> configuration) {
        if (configuration == null) {
            return new OffHeapDiskStore.Provider();
        }
        if (configuration instanceof OffHeapDiskStoreProviderConfiguration) {
            return new OffHeapDiskStore.Provider(((OffHeapDiskStoreProviderConfiguration) configuration).getThreadPoolAlias());
        }
        throw new IllegalArgumentException();
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<OffHeapDiskStore.Provider> getServiceType() {
        return OffHeapDiskStore.Provider.class;
    }
}
