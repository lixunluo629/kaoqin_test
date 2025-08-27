package org.ehcache.impl.config.copy;

import org.ehcache.impl.internal.classes.ClassInstanceConfiguration;
import org.ehcache.spi.copy.Copier;
import org.ehcache.spi.copy.CopyProvider;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/copy/DefaultCopierConfiguration.class */
public class DefaultCopierConfiguration<T> extends ClassInstanceConfiguration<Copier<T>> implements ServiceConfiguration<CopyProvider> {
    private final Type type;

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/copy/DefaultCopierConfiguration$Type.class */
    public enum Type {
        KEY,
        VALUE
    }

    public DefaultCopierConfiguration(Class<? extends Copier<T>> clazz, Type type) {
        super(clazz, new Object[0]);
        this.type = type;
    }

    public DefaultCopierConfiguration(Copier<T> instance, Type type) {
        super(instance);
        this.type = type;
    }

    DefaultCopierConfiguration(Class<? extends Copier<T>> copierClass) {
        super(copierClass, new Object[0]);
        this.type = null;
    }

    @Override // org.ehcache.spi.service.ServiceConfiguration
    public Class<CopyProvider> getServiceType() {
        return CopyProvider.class;
    }

    public Type getType() {
        return this.type;
    }
}
