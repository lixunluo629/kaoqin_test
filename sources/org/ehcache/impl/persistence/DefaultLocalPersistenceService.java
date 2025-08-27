package org.ehcache.impl.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import org.ehcache.CachePersistenceException;
import org.ehcache.core.spi.service.LocalPersistenceService;
import org.ehcache.impl.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.spi.service.MaintainableService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/persistence/DefaultLocalPersistenceService.class */
public class DefaultLocalPersistenceService implements LocalPersistenceService {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) DefaultLocalPersistenceService.class);
    private final File rootDirectory;
    private final File lockFile;
    private FileLock lock;
    private RandomAccessFile rw;
    private boolean started;

    public DefaultLocalPersistenceService(DefaultPersistenceConfiguration persistenceConfiguration) {
        if (persistenceConfiguration != null) {
            this.rootDirectory = persistenceConfiguration.getRootDirectory();
            this.lockFile = new File(this.rootDirectory, ".lock");
            return;
        }
        throw new NullPointerException("DefaultPersistenceConfiguration cannot be null");
    }

    @Override // org.ehcache.spi.service.Service
    public synchronized void start(ServiceProvider<Service> serviceProvider) throws IOException {
        internalStart();
    }

    @Override // org.ehcache.spi.service.MaintainableService
    public synchronized void startForMaintenance(ServiceProvider<? super MaintainableService> serviceProvider, MaintainableService.MaintenanceScope maintenanceScope) throws IOException {
        internalStart();
    }

    private void internalStart() throws IOException {
        if (!this.started) {
            FileUtils.createLocationIfRequiredAndVerify(this.rootDirectory);
            try {
                this.rw = new RandomAccessFile(this.lockFile, "rw");
                try {
                    this.lock = this.rw.getChannel().tryLock();
                    if (this.lock == null) {
                        throw new RuntimeException("Persistence directory already locked by another process: " + this.rootDirectory.getAbsolutePath());
                    }
                    this.started = true;
                    LOGGER.debug("RootDirectory Locked");
                } catch (OverlappingFileLockException e) {
                    throw new RuntimeException("Persistence directory already locked by this process: " + this.rootDirectory.getAbsolutePath(), e);
                } catch (Exception e2) {
                    try {
                        this.rw.close();
                    } catch (IOException e3) {
                    }
                    throw new RuntimeException("Persistence directory couldn't be locked: " + this.rootDirectory.getAbsolutePath(), e2);
                }
            } catch (FileNotFoundException e4) {
                throw new RuntimeException(e4);
            }
        }
    }

    @Override // org.ehcache.spi.service.Service
    public synchronized void stop() throws IOException {
        if (this.started) {
            try {
                this.lock.release();
                this.rw.close();
                if (!this.lockFile.delete()) {
                    LOGGER.debug("Lock file was not deleted {}.", this.lockFile.getPath());
                }
                this.started = false;
                LOGGER.debug("RootDirectory Unlocked");
            } catch (IOException e) {
                throw new RuntimeException("Couldn't unlock rootDir: " + this.rootDirectory.getAbsolutePath(), e);
            }
        }
    }

    File getLockFile() {
        return this.lockFile;
    }

    @Override // org.ehcache.core.spi.service.LocalPersistenceService
    public LocalPersistenceService.SafeSpaceIdentifier createSafeSpaceIdentifier(String owner, String identifier) {
        FileUtils.validateName(owner);
        SafeSpace ss = createSafeSpaceLogical(owner, identifier);
        File parentFile = ss.directory.getParentFile();
        while (true) {
            File parent = parentFile;
            if (parent != null) {
                if (!this.rootDirectory.equals(parent)) {
                    parentFile = parent.getParentFile();
                } else {
                    return new DefaultSafeSpaceIdentifier(ss);
                }
            } else {
                throw new IllegalArgumentException("Attempted to access file outside the persistence path");
            }
        }
    }

    @Override // org.ehcache.core.spi.service.LocalPersistenceService
    public void createSafeSpace(LocalPersistenceService.SafeSpaceIdentifier safeSpaceId) throws CachePersistenceException {
        if (safeSpaceId == null || !(safeSpaceId instanceof DefaultSafeSpaceIdentifier)) {
            throw new AssertionError("Invalid safe space identifier. Identifier not created");
        }
        SafeSpace ss = ((DefaultSafeSpaceIdentifier) safeSpaceId).safeSpace;
        FileUtils.create(ss.directory.getParentFile());
        FileUtils.create(ss.directory);
    }

    @Override // org.ehcache.core.spi.service.LocalPersistenceService
    public void destroySafeSpace(LocalPersistenceService.SafeSpaceIdentifier safeSpaceId, boolean verbose) {
        if (safeSpaceId == null || !(safeSpaceId instanceof DefaultSafeSpaceIdentifier)) {
            throw new AssertionError("Invalid safe space identifier. Identifier not created");
        }
        SafeSpace ss = ((DefaultSafeSpaceIdentifier) safeSpaceId).safeSpace;
        destroy(ss, verbose);
    }

    @Override // org.ehcache.core.spi.service.LocalPersistenceService
    public void destroyAll(String owner) {
        File ownerDirectory = new File(this.rootDirectory, owner);
        boolean cleared = true;
        if (ownerDirectory.exists() && ownerDirectory.isDirectory()) {
            cleared = false;
            if (FileUtils.recursiveDeleteDirectoryContent(ownerDirectory)) {
                LOGGER.debug("Destroyed all file based persistence contexts owned by {}", owner);
                cleared = ownerDirectory.delete();
            }
        }
        if (!cleared) {
            LOGGER.warn("Could not delete all file based persistence contexts owned by {}", owner);
        }
    }

    private void destroy(SafeSpace ss, boolean verbose) {
        if (verbose) {
            LOGGER.debug("Destroying file based persistence context for {}", ss.identifier);
        }
        if (!ss.directory.exists() || FileUtils.tryRecursiveDelete(ss.directory) || !verbose) {
            return;
        }
        LOGGER.warn("Could not delete directory for context {}", ss.identifier);
    }

    private SafeSpace createSafeSpaceLogical(String owner, String identifier) {
        File ownerDirectory = new File(this.rootDirectory, owner);
        File directory = new File(ownerDirectory, FileUtils.safeIdentifier(identifier));
        return new SafeSpace(identifier, directory);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/persistence/DefaultLocalPersistenceService$SafeSpace.class */
    private static final class SafeSpace {
        private final String identifier;
        private final File directory;

        private SafeSpace(String identifier, File directory) {
            this.directory = directory;
            this.identifier = identifier;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/persistence/DefaultLocalPersistenceService$DefaultSafeSpaceIdentifier.class */
    private static final class DefaultSafeSpaceIdentifier implements LocalPersistenceService.SafeSpaceIdentifier {
        private final SafeSpace safeSpace;

        private DefaultSafeSpaceIdentifier(SafeSpace safeSpace) {
            this.safeSpace = safeSpace;
        }

        public String toString() {
            return this.safeSpace.identifier;
        }

        @Override // org.ehcache.core.spi.service.LocalPersistenceService.SafeSpaceIdentifier
        public File getRoot() {
            return this.safeSpace.directory;
        }
    }
}
