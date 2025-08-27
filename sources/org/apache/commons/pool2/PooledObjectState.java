package org.apache.commons.pool2;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PooledObjectState.class */
public enum PooledObjectState {
    IDLE,
    ALLOCATED,
    EVICTION,
    EVICTION_RETURN_TO_HEAD,
    VALIDATION,
    VALIDATION_PREALLOCATED,
    VALIDATION_RETURN_TO_HEAD,
    INVALID,
    ABANDONED,
    RETURNING
}
