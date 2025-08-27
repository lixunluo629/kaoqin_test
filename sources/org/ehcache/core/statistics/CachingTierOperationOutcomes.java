package org.ehcache.core.statistics;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CachingTierOperationOutcomes.class */
public interface CachingTierOperationOutcomes {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CachingTierOperationOutcomes$GetOrComputeIfAbsentOutcome.class */
    public enum GetOrComputeIfAbsentOutcome implements CachingTierOperationOutcomes {
        HIT,
        FAULTED,
        FAULT_FAILED,
        FAULT_FAILED_MISS,
        MISS
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CachingTierOperationOutcomes$InvalidateAllOutcome.class */
    public enum InvalidateAllOutcome implements CachingTierOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CachingTierOperationOutcomes$InvalidateAllWithHashOutcome.class */
    public enum InvalidateAllWithHashOutcome implements CachingTierOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CachingTierOperationOutcomes$InvalidateOutcome.class */
    public enum InvalidateOutcome implements CachingTierOperationOutcomes {
        REMOVED,
        MISS
    }
}
