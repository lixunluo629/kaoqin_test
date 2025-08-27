package org.ehcache.core.spi.service;

import java.io.File;
import org.ehcache.CachePersistenceException;
import org.ehcache.spi.service.MaintainableService;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/service/LocalPersistenceService.class */
public interface LocalPersistenceService extends MaintainableService {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/service/LocalPersistenceService$SafeSpaceIdentifier.class */
    public interface SafeSpaceIdentifier {
        File getRoot();
    }

    SafeSpaceIdentifier createSafeSpaceIdentifier(String str, String str2);

    void createSafeSpace(SafeSpaceIdentifier safeSpaceIdentifier) throws CachePersistenceException;

    void destroySafeSpace(SafeSpaceIdentifier safeSpaceIdentifier, boolean z);

    void destroyAll(String str);
}
