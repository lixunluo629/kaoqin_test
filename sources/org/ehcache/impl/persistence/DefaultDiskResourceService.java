package org.ehcache.impl.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import org.ehcache.CachePersistenceException;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourceType;
import org.ehcache.config.SizedResourcePool;
import org.ehcache.core.spi.service.DiskResourceService;
import org.ehcache.core.spi.service.FileBasedPersistenceContext;
import org.ehcache.core.spi.service.LocalPersistenceService;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.ehcache.spi.persistence.PersistableResourceService;
import org.ehcache.spi.persistence.StateRepository;
import org.ehcache.spi.service.MaintainableService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceDependencies;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServiceDependencies({LocalPersistenceService.class})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/persistence/DefaultDiskResourceService.class */
public class DefaultDiskResourceService implements DiskResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) DefaultDiskResourceService.class);
    static final String PERSISTENCE_SPACE_OWNER = "file";
    private final ConcurrentMap<String, PersistenceSpace> knownPersistenceSpaces = new ConcurrentHashMap();
    private volatile LocalPersistenceService persistenceService;
    private volatile boolean isStarted;

    private boolean isStarted() {
        return this.isStarted;
    }

    @Override // org.ehcache.spi.service.Service
    public void start(ServiceProvider<Service> serviceProvider) {
        innerStart(serviceProvider);
    }

    @Override // org.ehcache.spi.service.MaintainableService
    public void startForMaintenance(ServiceProvider<? super MaintainableService> serviceProvider, MaintainableService.MaintenanceScope maintenanceScope) {
        innerStart(serviceProvider);
    }

    private void innerStart(ServiceProvider<? super MaintainableService> serviceProvider) {
        this.persistenceService = (LocalPersistenceService) serviceProvider.getService(LocalPersistenceService.class);
        this.isStarted = true;
    }

    @Override // org.ehcache.spi.service.Service
    public void stop() {
        this.isStarted = false;
        this.persistenceService = null;
    }

    @Override // org.ehcache.spi.persistence.PersistableResourceService
    public boolean handlesResourceType(ResourceType<?> resourceType) {
        return this.persistenceService != null && ResourceType.Core.DISK.equals(resourceType);
    }

    @Override // org.ehcache.spi.persistence.PersistableResourceService
    public PersistableResourceService.PersistenceSpaceIdentifier<DiskResourceService> getPersistenceSpaceIdentifier(String name, CacheConfiguration<?, ?> config) throws CachePersistenceException {
        PersistenceSpace newSpace;
        if (this.persistenceService == null) {
            return null;
        }
        boolean persistent = ((SizedResourcePool) config.getResourcePools().getPoolForResource(ResourceType.Core.DISK)).isPersistent();
        do {
            PersistenceSpace persistenceSpace = this.knownPersistenceSpaces.get(name);
            if (persistenceSpace != null) {
                return persistenceSpace.identifier;
            }
            newSpace = createSpace(name, persistent);
        } while (newSpace == null);
        return newSpace.identifier;
    }

    @Override // org.ehcache.spi.persistence.PersistableResourceService
    public void releasePersistenceSpaceIdentifier(PersistableResourceService.PersistenceSpaceIdentifier<?> identifier) throws CachePersistenceException {
        String name = null;
        for (Map.Entry<String, PersistenceSpace> entry : this.knownPersistenceSpaces.entrySet()) {
            if (entry.getValue().identifier.equals(identifier)) {
                name = entry.getKey();
            }
        }
        if (name == null) {
            throw newCachePersistenceException(identifier);
        }
        PersistenceSpace persistenceSpace = this.knownPersistenceSpaces.remove(name);
        if (persistenceSpace != null) {
            for (FileBasedStateRepository stateRepository : persistenceSpace.stateRepositories.values()) {
                try {
                    stateRepository.close();
                } catch (IOException e) {
                    LOGGER.warn("StateRepository close failed - destroying persistence space {} to prevent corruption", identifier, e);
                    this.persistenceService.destroySafeSpace(((DefaultPersistenceSpaceIdentifier) identifier).persistentSpaceId, true);
                }
            }
        }
    }

    private PersistenceSpace createSpace(String name, boolean persistent) throws CachePersistenceException {
        DefaultPersistenceSpaceIdentifier persistenceSpaceIdentifier = new DefaultPersistenceSpaceIdentifier(this.persistenceService.createSafeSpaceIdentifier("file", name));
        PersistenceSpace persistenceSpace = new PersistenceSpace(persistenceSpaceIdentifier);
        if (this.knownPersistenceSpaces.putIfAbsent(name, persistenceSpace) == null) {
            boolean created = false;
            if (!persistent) {
                try {
                    this.persistenceService.destroySafeSpace(persistenceSpaceIdentifier.persistentSpaceId, true);
                } catch (Throwable th) {
                    if (!created) {
                        this.knownPersistenceSpaces.remove(name, persistenceSpace);
                    }
                    throw th;
                }
            }
            this.persistenceService.createSafeSpace(persistenceSpaceIdentifier.persistentSpaceId);
            created = true;
            if (1 == 0) {
                this.knownPersistenceSpaces.remove(name, persistenceSpace);
            }
            return persistenceSpace;
        }
        return null;
    }

    private void checkStarted() {
        if (!isStarted()) {
            throw new IllegalStateException(getClass().getName() + " should be started to call destroy");
        }
    }

    @Override // org.ehcache.spi.persistence.PersistableResourceService
    public void destroy(String name) throws CachePersistenceException {
        checkStarted();
        if (this.persistenceService == null) {
            return;
        }
        PersistenceSpace space = this.knownPersistenceSpaces.remove(name);
        LocalPersistenceService.SafeSpaceIdentifier identifier = space == null ? this.persistenceService.createSafeSpaceIdentifier("file", name) : space.identifier.persistentSpaceId;
        this.persistenceService.destroySafeSpace(identifier, true);
    }

    @Override // org.ehcache.spi.persistence.PersistableResourceService
    public void destroyAll() {
        checkStarted();
        if (this.persistenceService == null) {
            return;
        }
        this.persistenceService.destroyAll("file");
    }

    @Override // org.ehcache.spi.persistence.PersistableResourceService
    public StateRepository getStateRepositoryWithin(PersistableResourceService.PersistenceSpaceIdentifier<?> identifier, String name) throws CachePersistenceException {
        PersistenceSpace persistenceSpace = getPersistenceSpace(identifier);
        if (persistenceSpace == null) {
            throw newCachePersistenceException(identifier);
        }
        FileBasedStateRepository stateRepository = new FileBasedStateRepository(FileUtils.createSubDirectory(persistenceSpace.identifier.persistentSpaceId.getRoot(), name));
        FileBasedStateRepository previous = persistenceSpace.stateRepositories.putIfAbsent(name, stateRepository);
        if (previous != null) {
            return previous;
        }
        return stateRepository;
    }

    private CachePersistenceException newCachePersistenceException(PersistableResourceService.PersistenceSpaceIdentifier<?> identifier) throws CachePersistenceException {
        return new CachePersistenceException("Unknown space: " + identifier);
    }

    private PersistenceSpace getPersistenceSpace(PersistableResourceService.PersistenceSpaceIdentifier<?> identifier) {
        for (PersistenceSpace persistenceSpace : this.knownPersistenceSpaces.values()) {
            if (persistenceSpace.identifier.equals(identifier)) {
                return persistenceSpace;
            }
        }
        return null;
    }

    @Override // org.ehcache.core.spi.service.DiskResourceService
    public FileBasedPersistenceContext createPersistenceContextWithin(PersistableResourceService.PersistenceSpaceIdentifier<?> identifier, String name) throws CachePersistenceException {
        if (getPersistenceSpace(identifier) == null) {
            throw newCachePersistenceException(identifier);
        }
        return new DefaultFileBasedPersistenceContext(FileUtils.createSubDirectory(((DefaultPersistenceSpaceIdentifier) identifier).persistentSpaceId.getRoot(), name));
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/persistence/DefaultDiskResourceService$PersistenceSpace.class */
    private static class PersistenceSpace {
        final DefaultPersistenceSpaceIdentifier identifier;
        final ConcurrentMap<String, FileBasedStateRepository> stateRepositories;

        private PersistenceSpace(DefaultPersistenceSpaceIdentifier identifier) {
            this.stateRepositories = new ConcurrentHashMap();
            this.identifier = identifier;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/persistence/DefaultDiskResourceService$DefaultPersistenceSpaceIdentifier.class */
    private static class DefaultPersistenceSpaceIdentifier implements PersistableResourceService.PersistenceSpaceIdentifier<DiskResourceService> {
        final LocalPersistenceService.SafeSpaceIdentifier persistentSpaceId;

        private DefaultPersistenceSpaceIdentifier(LocalPersistenceService.SafeSpaceIdentifier persistentSpaceId) {
            this.persistentSpaceId = persistentSpaceId;
        }

        @Override // org.ehcache.spi.service.ServiceConfiguration
        public Class<DiskResourceService> getServiceType() {
            return DiskResourceService.class;
        }

        public String toString() {
            return this.persistentSpaceId.toString();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/persistence/DefaultDiskResourceService$DefaultFileBasedPersistenceContext.class */
    private static class DefaultFileBasedPersistenceContext implements FileBasedPersistenceContext {
        private final File directory;

        private DefaultFileBasedPersistenceContext(File directory) {
            this.directory = directory;
        }

        @Override // org.ehcache.core.spi.service.FileBasedPersistenceContext
        public File getDirectory() {
            return this.directory;
        }
    }
}
