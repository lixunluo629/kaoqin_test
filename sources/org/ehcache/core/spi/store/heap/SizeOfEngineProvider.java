package org.ehcache.core.spi.store.heap;

import org.ehcache.config.ResourceUnit;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/heap/SizeOfEngineProvider.class */
public interface SizeOfEngineProvider extends Service {
    SizeOfEngine createSizeOfEngine(ResourceUnit resourceUnit, ServiceConfiguration<?>... serviceConfigurationArr);
}
