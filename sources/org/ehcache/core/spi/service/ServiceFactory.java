package org.ehcache.core.spi.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/service/ServiceFactory.class */
public interface ServiceFactory<T extends Service> {

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/service/ServiceFactory$RequiresConfiguration.class */
    public @interface RequiresConfiguration {
    }

    T create(ServiceCreationConfiguration<T> serviceCreationConfiguration);

    Class<? extends T> getServiceType();
}
