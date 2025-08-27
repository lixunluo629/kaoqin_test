package org.ehcache.impl.internal;

import org.ehcache.core.spi.time.SystemTimeSource;
import org.ehcache.core.spi.time.TimeSource;
import org.ehcache.core.spi.time.TimeSourceService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceProvider;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/DefaultTimeSourceService.class */
public class DefaultTimeSourceService implements TimeSourceService {
    private final TimeSource timeSource;

    public DefaultTimeSourceService(TimeSourceConfiguration config) {
        if (config != null) {
            this.timeSource = config.getTimeSource();
        } else {
            this.timeSource = SystemTimeSource.INSTANCE;
        }
    }

    @Override // org.ehcache.core.spi.time.TimeSourceService
    public TimeSource getTimeSource() {
        return this.timeSource;
    }

    @Override // org.ehcache.spi.service.Service
    public void start(ServiceProvider<Service> serviceProvider) {
    }

    @Override // org.ehcache.spi.service.Service
    public void stop() {
    }
}
