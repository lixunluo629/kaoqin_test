package org.ehcache.core.config;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.ehcache.config.ResourcePool;
import org.ehcache.config.ResourceType;
import org.ehcache.config.ResourceUnit;
import org.ehcache.config.SizedResourcePool;
import org.ehcache.core.HumanReadable;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/config/SizedResourcePoolImpl.class */
public class SizedResourcePoolImpl<P extends SizedResourcePool> extends AbstractResourcePool<P, ResourceType<P>> implements SizedResourcePool, HumanReadable {
    private final long size;
    private final ResourceUnit unit;

    public SizedResourcePoolImpl(ResourceType<P> type, long size, ResourceUnit unit, boolean persistent) {
        super(type, persistent);
        if (unit == null) {
            throw new NullPointerException("ResourceUnit can not be null");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        if (!type.isPersistable() && persistent) {
            throw new IllegalStateException("Non-persistable resource cannot be configured persistent");
        }
        this.size = size;
        this.unit = unit;
    }

    @Override // org.ehcache.config.SizedResourcePool
    public long getSize() {
        return this.size;
    }

    @Override // org.ehcache.config.SizedResourcePool
    public ResourceUnit getUnit() {
        return this.unit;
    }

    @Override // org.ehcache.core.config.AbstractResourcePool, org.ehcache.config.ResourcePool
    public void validateUpdate(ResourcePool newPool) {
        super.validateUpdate(newPool);
        SizedResourcePool sizedPool = (SizedResourcePool) newPool;
        if (!getUnit().getClass().equals(sizedPool.getUnit().getClass())) {
            throw new IllegalArgumentException("ResourcePool for " + sizedPool.getType() + " with ResourceUnit '" + sizedPool.getUnit() + "' can not replace '" + getUnit() + "'");
        }
        if (sizedPool.getSize() <= 0) {
            throw new IllegalArgumentException("ResourcePool for " + sizedPool.getType() + " must specify space greater than 0");
        }
    }

    public String toString() {
        return "Pool {" + getSize() + SymbolConstants.SPACE_SYMBOL + getUnit() + SymbolConstants.SPACE_SYMBOL + getType() + (isPersistent() ? "(persistent)}" : "}");
    }

    @Override // org.ehcache.core.HumanReadable
    public String readableString() {
        return getSize() + SymbolConstants.SPACE_SYMBOL + getUnit() + SymbolConstants.SPACE_SYMBOL + (isPersistent() ? "(persistent)" : "");
    }
}
