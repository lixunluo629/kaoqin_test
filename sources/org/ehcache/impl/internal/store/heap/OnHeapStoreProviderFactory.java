package org.ehcache.impl.internal.store.heap;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.internal.store.heap.OnHeapStore;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/OnHeapStoreProviderFactory.class */
public class OnHeapStoreProviderFactory implements ServiceFactory<OnHeapStore.Provider> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<OnHeapStore.Provider>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public OnHeapStore.Provider create(ServiceCreationConfiguration<OnHeapStore.Provider> configuration) {
        return new OnHeapStore.Provider();
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<OnHeapStore.Provider> getServiceType() {
        return OnHeapStore.Provider.class;
    }
}
