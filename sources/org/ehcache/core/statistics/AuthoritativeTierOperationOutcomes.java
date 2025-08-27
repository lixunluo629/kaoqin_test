package org.ehcache.core.statistics;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/AuthoritativeTierOperationOutcomes.class */
public interface AuthoritativeTierOperationOutcomes {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/AuthoritativeTierOperationOutcomes$ComputeIfAbsentAndFaultOutcome.class */
    public enum ComputeIfAbsentAndFaultOutcome implements AuthoritativeTierOperationOutcomes {
        HIT,
        PUT,
        NOOP
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/AuthoritativeTierOperationOutcomes$FlushOutcome.class */
    public enum FlushOutcome implements AuthoritativeTierOperationOutcomes {
        HIT,
        MISS
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/AuthoritativeTierOperationOutcomes$GetAndFaultOutcome.class */
    public enum GetAndFaultOutcome implements AuthoritativeTierOperationOutcomes {
        HIT,
        MISS,
        TIMEOUT
    }
}
