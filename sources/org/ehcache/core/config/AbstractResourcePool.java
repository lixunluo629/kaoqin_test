package org.ehcache.core.config;

import org.ehcache.config.ResourcePool;
import org.ehcache.config.ResourceType;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/config/AbstractResourcePool.class */
public abstract class AbstractResourcePool<P extends ResourcePool, T extends ResourceType<P>> implements ResourcePool {
    private final T type;
    private final boolean persistent;

    protected AbstractResourcePool(T type, boolean persistent) {
        if (type == null) {
            throw new NullPointerException("ResourceType may not be null");
        }
        this.type = type;
        this.persistent = persistent;
    }

    @Override // org.ehcache.config.ResourcePool
    public T getType() {
        return this.type;
    }

    @Override // org.ehcache.config.ResourcePool
    public boolean isPersistent() {
        return this.persistent;
    }

    @Override // org.ehcache.config.ResourcePool
    public void validateUpdate(ResourcePool newPool) {
        if (!getType().equals(newPool.getType())) {
            throw new IllegalArgumentException("ResourceType " + newPool.getType() + " can not replace " + getType());
        }
        if (isPersistent() != newPool.isPersistent()) {
            throw new IllegalArgumentException("ResourcePool for " + newPool.getType() + " with isPersistent=" + newPool.isPersistent() + " can not replace isPersistent=" + isPersistent());
        }
    }
}
