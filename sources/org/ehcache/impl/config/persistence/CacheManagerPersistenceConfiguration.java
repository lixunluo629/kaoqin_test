package org.ehcache.impl.config.persistence;

import java.io.File;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.CacheManagerConfiguration;
import org.ehcache.core.HumanReadable;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/persistence/CacheManagerPersistenceConfiguration.class */
public class CacheManagerPersistenceConfiguration extends DefaultPersistenceConfiguration implements CacheManagerConfiguration<PersistentCacheManager>, HumanReadable {
    public CacheManagerPersistenceConfiguration(File rootDirectory) {
        super(rootDirectory);
    }

    @Override // org.ehcache.config.builders.CacheManagerConfiguration
    public CacheManagerBuilder<PersistentCacheManager> builder(CacheManagerBuilder<? extends CacheManager> other) {
        return other.using(this);
    }

    @Override // org.ehcache.core.HumanReadable
    public String readableString() {
        return getClass().getName() + ":\n    rootDirectory: " + getRootDirectory();
    }
}
