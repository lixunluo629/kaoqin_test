package org.ehcache.core.statistics;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes.class */
public interface CacheOperationOutcomes {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes$CacheLoadingOutcome.class */
    public enum CacheLoadingOutcome implements CacheOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes$ClearOutcome.class */
    public enum ClearOutcome implements CacheOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes$ConditionalRemoveOutcome.class */
    public enum ConditionalRemoveOutcome implements CacheOperationOutcomes {
        SUCCESS,
        FAILURE_KEY_PRESENT,
        FAILURE_KEY_MISSING,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes$GetAllOutcome.class */
    public enum GetAllOutcome implements CacheOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes$GetOutcome.class */
    public enum GetOutcome implements CacheOperationOutcomes {
        HIT,
        MISS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes$PutAllOutcome.class */
    public enum PutAllOutcome implements CacheOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes$PutIfAbsentOutcome.class */
    public enum PutIfAbsentOutcome implements CacheOperationOutcomes {
        PUT,
        HIT,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes$PutOutcome.class */
    public enum PutOutcome implements CacheOperationOutcomes {
        PUT,
        UPDATED,
        NOOP,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes$RemoveAllOutcome.class */
    public enum RemoveAllOutcome implements CacheOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes$RemoveOutcome.class */
    public enum RemoveOutcome implements CacheOperationOutcomes {
        SUCCESS,
        NOOP,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/CacheOperationOutcomes$ReplaceOutcome.class */
    public enum ReplaceOutcome implements CacheOperationOutcomes {
        HIT,
        MISS_PRESENT,
        MISS_NOT_PRESENT,
        FAILURE
    }
}
