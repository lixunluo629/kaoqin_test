package org.ehcache.spi.service;

import org.ehcache.spi.service.Service;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/service/ServiceConfiguration.class */
public interface ServiceConfiguration<T extends Service> {
    Class<T> getServiceType();
}
