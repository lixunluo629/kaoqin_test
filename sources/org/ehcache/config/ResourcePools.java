package org.ehcache.config;

import java.util.Set;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/ResourcePools.class */
public interface ResourcePools {
    <P extends ResourcePool> P getPoolForResource(ResourceType<P> resourceType);

    Set<ResourceType<?>> getResourceTypeSet();

    ResourcePools validateAndMerge(ResourcePools resourcePools) throws UnsupportedOperationException, IllegalArgumentException;
}
