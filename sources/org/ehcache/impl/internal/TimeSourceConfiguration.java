package org.ehcache.impl.internal;

import org.ehcache.core.spi.time.TimeSource;
import org.ehcache.core.spi.time.TimeSourceService;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/TimeSourceConfiguration.class */
public class TimeSourceConfiguration implements ServiceCreationConfiguration<TimeSourceService> {
    private final TimeSource timeSource;

    public TimeSourceConfiguration(TimeSource timeSource) {
        this.timeSource = timeSource;
    }

    @Override // org.ehcache.spi.service.ServiceCreationConfiguration
    public Class<TimeSourceService> getServiceType() {
        return TimeSourceService.class;
    }

    public TimeSource getTimeSource() {
        return this.timeSource;
    }
}
