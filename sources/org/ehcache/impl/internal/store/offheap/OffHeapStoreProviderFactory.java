package org.ehcache.impl.internal.store.offheap;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.internal.store.offheap.OffHeapStore;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/OffHeapStoreProviderFactory.class */
public class OffHeapStoreProviderFactory implements ServiceFactory<OffHeapStore.Provider> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<OffHeapStore.Provider>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public OffHeapStore.Provider create(ServiceCreationConfiguration<OffHeapStore.Provider> configuration) {
        return new OffHeapStore.Provider();
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<OffHeapStore.Provider> getServiceType() {
        return OffHeapStore.Provider.class;
    }
}
