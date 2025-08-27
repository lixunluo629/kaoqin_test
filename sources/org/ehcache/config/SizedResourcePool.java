package org.ehcache.config;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/SizedResourcePool.class */
public interface SizedResourcePool extends ResourcePool {
    long getSize();

    ResourceUnit getUnit();
}
