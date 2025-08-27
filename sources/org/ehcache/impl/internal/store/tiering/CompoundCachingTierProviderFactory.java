package org.ehcache.impl.internal.store.tiering;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.internal.store.tiering.CompoundCachingTier;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/tiering/CompoundCachingTierProviderFactory.class */
public class CompoundCachingTierProviderFactory implements ServiceFactory<CompoundCachingTier.Provider> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<CompoundCachingTier.Provider>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public CompoundCachingTier.Provider create(ServiceCreationConfiguration<CompoundCachingTier.Provider> serviceConfiguration) {
        return new CompoundCachingTier.Provider();
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<CompoundCachingTier.Provider> getServiceType() {
        return CompoundCachingTier.Provider.class;
    }
}
