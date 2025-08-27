package org.ehcache.config.builders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.ehcache.config.Builder;
import org.ehcache.config.ResourcePool;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.ResourceType;
import org.ehcache.config.ResourceUnit;
import org.ehcache.config.SizedResourcePool;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.ResourcePoolsImpl;
import org.ehcache.core.config.SizedResourcePoolImpl;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/ResourcePoolsBuilder.class */
public class ResourcePoolsBuilder implements Builder<ResourcePools> {
    private final Map<ResourceType<?>, ResourcePool> resourcePools;

    private ResourcePoolsBuilder() {
        this(Collections.emptyMap());
    }

    private ResourcePoolsBuilder(Map<ResourceType<?>, ResourcePool> resourcePools) {
        ResourcePoolsImpl.validateResourcePools(resourcePools.values());
        this.resourcePools = Collections.unmodifiableMap(resourcePools);
    }

    public static ResourcePoolsBuilder newResourcePoolsBuilder() {
        return new ResourcePoolsBuilder();
    }

    public static ResourcePoolsBuilder newResourcePoolsBuilder(ResourcePools pools) {
        ResourcePoolsBuilder poolsBuilder = new ResourcePoolsBuilder();
        for (ResourceType<?> currentResourceType : pools.getResourceTypeSet()) {
            poolsBuilder = poolsBuilder.with(pools.getPoolForResource(currentResourceType));
        }
        return poolsBuilder;
    }

    public static ResourcePoolsBuilder heap(long entries) {
        return newResourcePoolsBuilder().heap(entries, EntryUnit.ENTRIES);
    }

    public ResourcePoolsBuilder with(ResourcePool resourcePool) {
        ResourceType<?> type = resourcePool.getType();
        ResourcePool existingPool = this.resourcePools.get(type);
        if (existingPool != null) {
            throw new IllegalArgumentException("Can not add '" + resourcePool + "'; configuration already contains '" + existingPool + "'");
        }
        Map<ResourceType<?>, ResourcePool> newPools = new HashMap<>(this.resourcePools);
        newPools.put(type, resourcePool);
        return new ResourcePoolsBuilder(newPools);
    }

    public ResourcePoolsBuilder withReplacing(ResourcePool resourcePool) {
        Map<ResourceType<?>, ResourcePool> newPools = new HashMap<>(this.resourcePools);
        newPools.put(resourcePool.getType(), resourcePool);
        return new ResourcePoolsBuilder(newPools);
    }

    public ResourcePoolsBuilder with(ResourceType<SizedResourcePool> type, long size, ResourceUnit unit, boolean persistent) {
        return with(new SizedResourcePoolImpl(type, size, unit, persistent));
    }

    public ResourcePoolsBuilder heap(long size, ResourceUnit unit) {
        return with(ResourceType.Core.HEAP, size, unit, false);
    }

    public ResourcePoolsBuilder offheap(long size, MemoryUnit unit) {
        return with(ResourceType.Core.OFFHEAP, size, unit, false);
    }

    public ResourcePoolsBuilder disk(long size, MemoryUnit unit) {
        return disk(size, unit, false);
    }

    public ResourcePoolsBuilder disk(long size, MemoryUnit unit, boolean persistent) {
        return with(ResourceType.Core.DISK, size, unit, persistent);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ehcache.config.Builder
    /* renamed from: build */
    public ResourcePools build2() {
        return new ResourcePoolsImpl(this.resourcePools);
    }
}
