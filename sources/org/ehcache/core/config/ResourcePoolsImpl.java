package org.ehcache.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.ehcache.config.ResourcePool;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.ResourceType;
import org.ehcache.config.SizedResourcePool;
import org.ehcache.core.HumanReadable;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/config/ResourcePoolsImpl.class */
public class ResourcePoolsImpl implements ResourcePools, HumanReadable {
    private final Map<ResourceType<?>, ResourcePool> pools;

    public ResourcePoolsImpl(Map<ResourceType<?>, ResourcePool> pools) {
        if (pools.isEmpty()) {
            throw new IllegalArgumentException("No resource pools defined");
        }
        validateResourcePools(pools.values());
        this.pools = pools;
    }

    @Override // org.ehcache.config.ResourcePools
    public <P extends ResourcePool> P getPoolForResource(ResourceType<P> resourceType) {
        return (P) resourceType.getResourcePoolClass().cast(this.pools.get(resourceType));
    }

    @Override // org.ehcache.config.ResourcePools
    public Set<ResourceType<?>> getResourceTypeSet() {
        return this.pools.keySet();
    }

    @Override // org.ehcache.config.ResourcePools
    public ResourcePools validateAndMerge(ResourcePools toBeUpdated) {
        if (!getResourceTypeSet().containsAll(toBeUpdated.getResourceTypeSet())) {
            throw new IllegalArgumentException("Pools to be updated cannot contain previously undefined resources pools");
        }
        if (toBeUpdated.getResourceTypeSet().contains(ResourceType.Core.OFFHEAP)) {
            throw new UnsupportedOperationException("Updating OFFHEAP resource is not supported");
        }
        if (toBeUpdated.getResourceTypeSet().contains(ResourceType.Core.DISK)) {
            throw new UnsupportedOperationException("Updating DISK resource is not supported");
        }
        for (ResourceType<?> currentResourceType : toBeUpdated.getResourceTypeSet()) {
            getPoolForResource(currentResourceType).validateUpdate(toBeUpdated.getPoolForResource(currentResourceType));
        }
        Map<ResourceType<?>, ResourcePool> poolsMap = new HashMap<>();
        poolsMap.putAll(this.pools);
        for (ResourceType<?> currentResourceType2 : toBeUpdated.getResourceTypeSet()) {
            ResourcePool poolForResource = toBeUpdated.getPoolForResource(currentResourceType2);
            poolsMap.put(currentResourceType2, poolForResource);
        }
        return new ResourcePoolsImpl(poolsMap);
    }

    public static void validateResourcePools(Collection<? extends ResourcePool> pools) {
        boolean ambiguity;
        boolean inversion;
        List<SizedResourcePool> ordered = new ArrayList<>(pools.size());
        for (ResourcePool pool : pools) {
            if (pool instanceof SizedResourcePool) {
                ordered.add((SizedResourcePool) pool);
            }
        }
        Collections.sort(ordered, new Comparator<SizedResourcePool>() { // from class: org.ehcache.core.config.ResourcePoolsImpl.1
            @Override // java.util.Comparator
            public int compare(SizedResourcePool o1, SizedResourcePool o2) {
                int retVal = o2.getType().getTierHeight() - o1.getType().getTierHeight();
                if (retVal == 0) {
                    return o1.toString().compareTo(o2.toString());
                }
                return retVal;
            }
        });
        for (int i = 0; i < ordered.size(); i++) {
            for (int j = 0; j < i; j++) {
                SizedResourcePool upper = ordered.get(j);
                SizedResourcePool lower = ordered.get(i);
                try {
                    ambiguity = upper.getType().getTierHeight() == lower.getType().getTierHeight();
                    inversion = upper.getUnit().compareTo(upper.getSize(), lower.getSize(), lower.getUnit()) >= 0 || lower.getUnit().compareTo(lower.getSize(), upper.getSize(), upper.getUnit()) <= 0;
                } catch (IllegalArgumentException e) {
                    ambiguity = false;
                    inversion = false;
                }
                if (ambiguity) {
                    throw new IllegalArgumentException("Tiering Ambiguity: '" + upper + "' has the same tier height as '" + lower + "'");
                }
                if (inversion) {
                    throw new IllegalArgumentException("Tiering Inversion: '" + upper + "' is not smaller than '" + lower + "'");
                }
            }
        }
    }

    @Override // org.ehcache.core.HumanReadable
    public String readableString() {
        Map<ResourceType<?>, ResourcePool> sortedPools = new TreeMap<>(new Comparator<ResourceType<?>>() { // from class: org.ehcache.core.config.ResourcePoolsImpl.2
            @Override // java.util.Comparator
            public int compare(ResourceType<?> o1, ResourceType<?> o2) {
                return o2.getTierHeight() - o1.getTierHeight();
            }
        });
        sortedPools.putAll(this.pools);
        StringBuilder poolsToStringBuilder = new StringBuilder();
        for (Map.Entry<ResourceType<?>, ResourcePool> poolEntry : sortedPools.entrySet()) {
            poolsToStringBuilder.append(poolEntry.getKey()).append(": ").append("\n        ").append("size: ").append(poolEntry.getValue() instanceof HumanReadable ? ((HumanReadable) poolEntry.getValue()).readableString() : poolEntry.getValue()).append("\n        ").append("tierHeight: ").append(poolEntry.getKey().getTierHeight()).append("\n    ");
        }
        if (poolsToStringBuilder.length() > 4) {
            poolsToStringBuilder.delete(poolsToStringBuilder.length() - 5, poolsToStringBuilder.length());
        }
        return "pools: \n    " + poolsToStringBuilder.toString();
    }
}
