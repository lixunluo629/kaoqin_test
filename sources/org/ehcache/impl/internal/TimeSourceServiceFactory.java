package org.ehcache.impl.internal;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.core.spi.time.TimeSourceService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/TimeSourceServiceFactory.class */
public class TimeSourceServiceFactory implements ServiceFactory<TimeSourceService> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<TimeSourceService>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public TimeSourceService create(ServiceCreationConfiguration<TimeSourceService> configuration) {
        return new DefaultTimeSourceService((TimeSourceConfiguration) configuration);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<TimeSourceService> getServiceType() {
        return TimeSourceService.class;
    }
}
