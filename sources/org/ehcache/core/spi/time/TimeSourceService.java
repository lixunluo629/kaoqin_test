package org.ehcache.core.spi.time;

import org.ehcache.spi.service.Service;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/time/TimeSourceService.class */
public interface TimeSourceService extends Service {
    TimeSource getTimeSource();
}
