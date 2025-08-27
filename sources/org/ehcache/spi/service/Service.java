package org.ehcache.spi.service;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/service/Service.class */
public interface Service {
    void start(ServiceProvider<Service> serviceProvider);

    void stop();
}
