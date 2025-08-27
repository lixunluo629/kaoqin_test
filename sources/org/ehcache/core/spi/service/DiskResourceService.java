package org.ehcache.core.spi.service;

import org.ehcache.CachePersistenceException;
import org.ehcache.spi.persistence.PersistableResourceService;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/service/DiskResourceService.class */
public interface DiskResourceService extends PersistableResourceService {
    FileBasedPersistenceContext createPersistenceContextWithin(PersistableResourceService.PersistenceSpaceIdentifier<?> persistenceSpaceIdentifier, String str) throws CachePersistenceException;
}
