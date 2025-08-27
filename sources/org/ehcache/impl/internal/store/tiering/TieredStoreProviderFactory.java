package org.ehcache.impl.internal.store.tiering;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.internal.store.tiering.TieredStore;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/tiering/TieredStoreProviderFactory.class */
public class TieredStoreProviderFactory implements ServiceFactory<TieredStore.Provider> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<TieredStore.Provider>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public TieredStore.Provider create(ServiceCreationConfiguration<TieredStore.Provider> configuration) {
        return new TieredStore.Provider();
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<TieredStore.Provider> getServiceType() {
        return TieredStore.Provider.class;
    }
}
