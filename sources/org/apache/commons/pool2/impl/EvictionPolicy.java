package org.apache.commons.pool2.impl;

import org.apache.commons.pool2.PooledObject;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/EvictionPolicy.class */
public interface EvictionPolicy<T> {
    boolean evict(EvictionConfig evictionConfig, PooledObject<T> pooledObject, int i);
}
