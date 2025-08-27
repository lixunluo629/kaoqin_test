package org.ehcache.impl.internal.persistence;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.persistence.DefaultDiskResourceService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/persistence/DefaultDiskResourceServiceFactory.class */
public class DefaultDiskResourceServiceFactory implements ServiceFactory<DefaultDiskResourceService> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<DefaultDiskResourceService>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public DefaultDiskResourceService create(ServiceCreationConfiguration<DefaultDiskResourceService> serviceConfiguration) {
        return new DefaultDiskResourceService();
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<DefaultDiskResourceService> getServiceType() {
        return DefaultDiskResourceService.class;
    }
}
