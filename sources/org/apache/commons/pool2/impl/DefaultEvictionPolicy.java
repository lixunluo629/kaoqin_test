package org.apache.commons.pool2.impl;

import org.apache.commons.pool2.PooledObject;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/DefaultEvictionPolicy.class */
public class DefaultEvictionPolicy<T> implements EvictionPolicy<T> {
    @Override // org.apache.commons.pool2.impl.EvictionPolicy
    public boolean evict(EvictionConfig config, PooledObject<T> underTest, int idleCount) {
        if ((config.getIdleSoftEvictTime() < underTest.getIdleTimeMillis() && config.getMinIdle() < idleCount) || config.getIdleEvictTime() < underTest.getIdleTimeMillis()) {
            return true;
        }
        return false;
    }
}
