package org.ehcache.impl.internal.spi.copy;

import java.util.Collection;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.impl.config.copy.DefaultCopierConfiguration;
import org.ehcache.impl.config.copy.DefaultCopyProviderConfiguration;
import org.ehcache.impl.copy.IdentityCopier;
import org.ehcache.impl.copy.SerializingCopier;
import org.ehcache.impl.internal.classes.ClassInstanceConfiguration;
import org.ehcache.impl.internal.classes.ClassInstanceProvider;
import org.ehcache.spi.copy.Copier;
import org.ehcache.spi.copy.CopyProvider;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.service.ServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/spi/copy/DefaultCopyProvider.class */
public class DefaultCopyProvider extends ClassInstanceProvider<Class<?>, Copier<?>> implements CopyProvider {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) DefaultCopyProvider.class);

    public DefaultCopyProvider(DefaultCopyProviderConfiguration configuration) {
        super(configuration, DefaultCopierConfiguration.class);
    }

    @Override // org.ehcache.spi.copy.CopyProvider
    public <T> Copier<T> createKeyCopier(Class<T> clazz, Serializer<T> serializer, ServiceConfiguration<?>... configs) {
        return createCopier(DefaultCopierConfiguration.Type.KEY, clazz, serializer, configs);
    }

    @Override // org.ehcache.spi.copy.CopyProvider
    public <T> Copier<T> createValueCopier(Class<T> clazz, Serializer<T> serializer, ServiceConfiguration<?>... configs) {
        return createCopier(DefaultCopierConfiguration.Type.VALUE, clazz, serializer, configs);
    }

    @Override // org.ehcache.spi.copy.CopyProvider
    public void releaseCopier(Copier<?> copier) throws Exception {
        if (!(copier instanceof SerializingCopier)) {
            releaseInstance(copier);
        }
    }

    private <T> Copier<T> createCopier(DefaultCopierConfiguration.Type type, Class<T> clazz, Serializer<T> serializer, ServiceConfiguration<?>... configs) {
        Copier<T> copier;
        DefaultCopierConfiguration<T> conf = find(type, configs);
        ClassInstanceConfiguration<Copier<?>> preConfigured = (ClassInstanceConfiguration) this.preconfigured.get(clazz);
        if (conf != null && conf.getClazz().isAssignableFrom(SerializingCopier.class)) {
            if (serializer == null) {
                throw new IllegalStateException("No Serializer configured for type '" + clazz.getName() + "' which doesn't implement java.io.Serializable");
            }
            copier = new SerializingCopier(serializer);
        } else if (conf == null && preConfigured != null && preConfigured.getClazz().isAssignableFrom(SerializingCopier.class)) {
            if (serializer == null) {
                throw new IllegalStateException("No Serializer configured for type '" + clazz.getName() + "' which doesn't implement java.io.Serializable");
            }
            copier = new SerializingCopier(serializer);
        } else {
            copier = createCopier(clazz, conf, type);
        }
        LOG.debug("Copier for <{}> : {}", clazz.getName(), copier);
        return copier;
    }

    private <T> Copier<T> createCopier(Class<T> cls, DefaultCopierConfiguration<T> defaultCopierConfiguration, DefaultCopierConfiguration.Type type) {
        Copier<?> copierNewInstance = newInstance((DefaultCopyProvider) cls, (ServiceConfiguration<?>) defaultCopierConfiguration);
        if (copierNewInstance == null) {
            copierNewInstance = newInstance((DefaultCopyProvider) cls, (ServiceConfiguration<?>) new DefaultCopierConfiguration(IdentityCopier.class, type));
        }
        return (Copier<T>) copierNewInstance;
    }

    private static <T> DefaultCopierConfiguration<T> find(DefaultCopierConfiguration.Type type, ServiceConfiguration<?>... serviceConfigurations) {
        DefaultCopierConfiguration<T> result = null;
        Collection<DefaultCopierConfiguration> copierConfigurations = ServiceUtils.findAmongst(DefaultCopierConfiguration.class, serviceConfigurations);
        for (DefaultCopierConfiguration copierConfiguration : copierConfigurations) {
            if (copierConfiguration.getType() == type) {
                if (result != null) {
                    throw new IllegalArgumentException("Duplicate " + type + " copier : " + copierConfiguration);
                }
                result = copierConfiguration;
            }
        }
        return result;
    }
}
