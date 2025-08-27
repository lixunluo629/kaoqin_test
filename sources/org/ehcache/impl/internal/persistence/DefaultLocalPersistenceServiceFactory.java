package org.ehcache.impl.internal.persistence;

import org.ehcache.core.spi.service.LocalPersistenceService;
import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.impl.persistence.DefaultLocalPersistenceService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

@ServiceFactory.RequiresConfiguration
/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/persistence/DefaultLocalPersistenceServiceFactory.class */
public class DefaultLocalPersistenceServiceFactory implements ServiceFactory<LocalPersistenceService> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<LocalPersistenceService>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public DefaultLocalPersistenceService create(ServiceCreationConfiguration<LocalPersistenceService> serviceConfiguration) {
        return new DefaultLocalPersistenceService((DefaultPersistenceConfiguration) serviceConfiguration);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<LocalPersistenceService> getServiceType() {
        return LocalPersistenceService.class;
    }
}
