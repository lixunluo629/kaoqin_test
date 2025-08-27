package org.ehcache.config;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/ResourcePool.class */
public interface ResourcePool {
    ResourceType<?> getType();

    boolean isPersistent();

    void validateUpdate(ResourcePool resourcePool);
}
