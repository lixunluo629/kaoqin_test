package org.ehcache.spi.persistence;

import org.ehcache.CachePersistenceException;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourceType;
import org.ehcache.spi.service.MaintainableService;
import org.ehcache.spi.service.PluralService;
import org.ehcache.spi.service.ServiceConfiguration;

@PluralService
/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/persistence/PersistableResourceService.class */
public interface PersistableResourceService extends MaintainableService {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/persistence/PersistableResourceService$PersistenceSpaceIdentifier.class */
    public interface PersistenceSpaceIdentifier<T extends PersistableResourceService> extends ServiceConfiguration<T> {
    }

    boolean handlesResourceType(ResourceType<?> resourceType);

    PersistenceSpaceIdentifier<?> getPersistenceSpaceIdentifier(String str, CacheConfiguration<?, ?> cacheConfiguration) throws CachePersistenceException;

    void releasePersistenceSpaceIdentifier(PersistenceSpaceIdentifier<?> persistenceSpaceIdentifier) throws CachePersistenceException;

    StateRepository getStateRepositoryWithin(PersistenceSpaceIdentifier<?> persistenceSpaceIdentifier, String str) throws CachePersistenceException;

    void destroy(String str) throws CachePersistenceException;

    void destroyAll() throws CachePersistenceException;
}
