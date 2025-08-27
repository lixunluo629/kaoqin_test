package org.ehcache.core.statistics;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/HigherCachingTierOperationOutcomes.class */
public interface HigherCachingTierOperationOutcomes {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/HigherCachingTierOperationOutcomes$SilentInvalidateAllOutcome.class */
    public enum SilentInvalidateAllOutcome implements HigherCachingTierOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/HigherCachingTierOperationOutcomes$SilentInvalidateAllWithHashOutcome.class */
    public enum SilentInvalidateAllWithHashOutcome implements HigherCachingTierOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/HigherCachingTierOperationOutcomes$SilentInvalidateOutcome.class */
    public enum SilentInvalidateOutcome implements HigherCachingTierOperationOutcomes {
        REMOVED,
        MISS
    }
}
