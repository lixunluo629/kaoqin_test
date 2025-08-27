package org.ehcache.impl.config.copy;

import org.ehcache.impl.internal.classes.ClassInstanceConfiguration;
import org.ehcache.impl.internal.classes.ClassInstanceProviderConfiguration;
import org.ehcache.spi.copy.Copier;
import org.ehcache.spi.copy.CopyProvider;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/copy/DefaultCopyProviderConfiguration.class */
public class DefaultCopyProviderConfiguration extends ClassInstanceProviderConfiguration<Class<?>, Copier<?>> implements ServiceCreationConfiguration<CopyProvider> {
    public DefaultCopyProviderConfiguration() {
    }

    public DefaultCopyProviderConfiguration(DefaultCopyProviderConfiguration other) {
        getDefaults().putAll(other.getDefaults());
    }

    @Override // org.ehcache.spi.service.ServiceCreationConfiguration
    public Class<CopyProvider> getServiceType() {
        return CopyProvider.class;
    }

    public <T> DefaultCopyProviderConfiguration addCopierFor(Class<T> clazz, Class<? extends Copier<T>> copierClass) {
        return addCopierFor(clazz, copierClass, false);
    }

    public <T> DefaultCopyProviderConfiguration addCopierFor(Class<T> clazz, Class<? extends Copier<T>> copierClass, boolean overwrite) {
        if (clazz == null) {
            throw new NullPointerException("Copy target class cannot be null");
        }
        if (copierClass == null) {
            throw new NullPointerException("Copier class cannot be null");
        }
        if (!overwrite && getDefaults().containsKey(clazz)) {
            throw new IllegalArgumentException("Duplicate copier for class : " + clazz);
        }
        ClassInstanceConfiguration<Copier<?>> configuration = new DefaultCopierConfiguration<>(copierClass);
        getDefaults().put(clazz, configuration);
        return this;
    }
}
