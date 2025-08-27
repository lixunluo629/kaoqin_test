package org.ehcache.core.statistics;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes.class */
public interface StoreOperationOutcomes {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes$ComputeIfAbsentOutcome.class */
    public enum ComputeIfAbsentOutcome implements StoreOperationOutcomes {
        HIT,
        PUT,
        NOOP
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes$ComputeOutcome.class */
    public enum ComputeOutcome implements StoreOperationOutcomes {
        HIT,
        MISS,
        PUT,
        REMOVED
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes$ConditionalRemoveOutcome.class */
    public enum ConditionalRemoveOutcome implements StoreOperationOutcomes {
        REMOVED,
        MISS
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes$ConditionalReplaceOutcome.class */
    public enum ConditionalReplaceOutcome implements StoreOperationOutcomes {
        REPLACED,
        MISS
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes$EvictionOutcome.class */
    public enum EvictionOutcome implements StoreOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes$ExpirationOutcome.class */
    public enum ExpirationOutcome implements StoreOperationOutcomes {
        SUCCESS
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes$GetOutcome.class */
    public enum GetOutcome implements StoreOperationOutcomes {
        HIT,
        MISS,
        TIMEOUT
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes$PutIfAbsentOutcome.class */
    public enum PutIfAbsentOutcome implements StoreOperationOutcomes {
        PUT,
        HIT
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes$PutOutcome.class */
    public enum PutOutcome implements StoreOperationOutcomes {
        PUT,
        REPLACED,
        NOOP
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes$RemoveOutcome.class */
    public enum RemoveOutcome implements StoreOperationOutcomes {
        REMOVED,
        MISS
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/StoreOperationOutcomes$ReplaceOutcome.class */
    public enum ReplaceOutcome implements StoreOperationOutcomes {
        REPLACED,
        MISS
    }
}
