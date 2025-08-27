package org.ehcache.impl.config.persistence;

import java.io.File;
import org.ehcache.core.spi.service.LocalPersistenceService;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/persistence/DefaultPersistenceConfiguration.class */
public class DefaultPersistenceConfiguration implements ServiceCreationConfiguration<LocalPersistenceService> {
    private final File rootDirectory;

    public DefaultPersistenceConfiguration(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public File getRootDirectory() {
        return this.rootDirectory;
    }

    @Override // org.ehcache.spi.service.ServiceCreationConfiguration
    public Class<LocalPersistenceService> getServiceType() {
        return LocalPersistenceService.class;
    }
}
