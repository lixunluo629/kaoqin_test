package org.ehcache.core.statistics;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/LowerCachingTierOperationsOutcome.class */
public interface LowerCachingTierOperationsOutcome {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/LowerCachingTierOperationsOutcome$GetAndRemoveOutcome.class */
    public enum GetAndRemoveOutcome implements LowerCachingTierOperationsOutcome {
        HIT_REMOVED,
        MISS
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/LowerCachingTierOperationsOutcome$InstallMappingOutcome.class */
    public enum InstallMappingOutcome implements LowerCachingTierOperationsOutcome {
        PUT,
        NOOP
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/LowerCachingTierOperationsOutcome$InvalidateAllOutcome.class */
    public enum InvalidateAllOutcome implements LowerCachingTierOperationsOutcome {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/LowerCachingTierOperationsOutcome$InvalidateAllWithHashOutcome.class */
    public enum InvalidateAllWithHashOutcome implements LowerCachingTierOperationsOutcome {
        SUCCESS
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/LowerCachingTierOperationsOutcome$InvalidateOutcome.class */
    public enum InvalidateOutcome implements LowerCachingTierOperationsOutcome {
        REMOVED,
        MISS
    }
}
