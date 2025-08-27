package org.ehcache.spi.copy;

import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/copy/CopyProvider.class */
public interface CopyProvider extends Service {
    <T> Copier<T> createKeyCopier(Class<T> cls, Serializer<T> serializer, ServiceConfiguration<?>... serviceConfigurationArr);

    <T> Copier<T> createValueCopier(Class<T> cls, Serializer<T> serializer, ServiceConfiguration<?>... serviceConfigurationArr);

    void releaseCopier(Copier<?> copier) throws Exception;
}
