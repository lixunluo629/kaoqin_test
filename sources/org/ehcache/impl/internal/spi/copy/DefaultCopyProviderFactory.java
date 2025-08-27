package org.ehcache.impl.internal.spi.copy;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.config.copy.DefaultCopyProviderConfiguration;
import org.ehcache.spi.copy.CopyProvider;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/spi/copy/DefaultCopyProviderFactory.class */
public class DefaultCopyProviderFactory implements ServiceFactory<CopyProvider> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<CopyProvider>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public CopyProvider create(ServiceCreationConfiguration<CopyProvider> configuration) {
        if (configuration != null && !(configuration instanceof DefaultCopyProviderConfiguration)) {
            throw new IllegalArgumentException("Expected a configuration of type DefaultCopyProviderConfiguration but got " + configuration.getClass().getSimpleName());
        }
        return new DefaultCopyProvider((DefaultCopyProviderConfiguration) configuration);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<CopyProvider> getServiceType() {
        return CopyProvider.class;
    }
}
