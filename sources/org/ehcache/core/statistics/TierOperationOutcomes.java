package org.ehcache.core.statistics;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.ehcache.core.statistics.AuthoritativeTierOperationOutcomes;
import org.ehcache.core.statistics.CachingTierOperationOutcomes;
import org.ehcache.core.statistics.LowerCachingTierOperationsOutcome;
import org.ehcache.core.statistics.StoreOperationOutcomes;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/TierOperationOutcomes.class */
public class TierOperationOutcomes {
    public static final Map<GetOutcome, Set<StoreOperationOutcomes.GetOutcome>> GET_TRANSLATION;
    public static final Map<GetOutcome, Set<AuthoritativeTierOperationOutcomes.GetAndFaultOutcome>> GET_AND_FAULT_TRANSLATION;
    public static final Map<GetOutcome, Set<LowerCachingTierOperationsOutcome.GetAndRemoveOutcome>> GET_AND_REMOVE_TRANSLATION;
    public static final Map<GetOutcome, Set<CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome>> GET_OR_COMPUTEIFABSENT_TRANSLATION;
    public static final Map<EvictionOutcome, Set<StoreOperationOutcomes.EvictionOutcome>> EVICTION_TRANSLATION;

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/TierOperationOutcomes$EvictionOutcome.class */
    public enum EvictionOutcome {
        SUCCESS,
        FAILURE
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/statistics/TierOperationOutcomes$GetOutcome.class */
    public enum GetOutcome {
        HIT,
        MISS
    }

    static {
        Map<GetOutcome, Set<StoreOperationOutcomes.GetOutcome>> translation = new EnumMap<>(GetOutcome.class);
        translation.put(GetOutcome.HIT, EnumSet.of(StoreOperationOutcomes.GetOutcome.HIT));
        translation.put(GetOutcome.MISS, EnumSet.of(StoreOperationOutcomes.GetOutcome.MISS, StoreOperationOutcomes.GetOutcome.TIMEOUT));
        GET_TRANSLATION = Collections.unmodifiableMap(translation);
        Map<GetOutcome, Set<AuthoritativeTierOperationOutcomes.GetAndFaultOutcome>> translation2 = new EnumMap<>(GetOutcome.class);
        translation2.put(GetOutcome.HIT, EnumSet.of(AuthoritativeTierOperationOutcomes.GetAndFaultOutcome.HIT));
        translation2.put(GetOutcome.MISS, EnumSet.of(AuthoritativeTierOperationOutcomes.GetAndFaultOutcome.MISS, AuthoritativeTierOperationOutcomes.GetAndFaultOutcome.TIMEOUT));
        GET_AND_FAULT_TRANSLATION = Collections.unmodifiableMap(translation2);
        Map<GetOutcome, Set<LowerCachingTierOperationsOutcome.GetAndRemoveOutcome>> translation3 = new EnumMap<>(GetOutcome.class);
        translation3.put(GetOutcome.HIT, EnumSet.of(LowerCachingTierOperationsOutcome.GetAndRemoveOutcome.HIT_REMOVED));
        translation3.put(GetOutcome.MISS, EnumSet.of(LowerCachingTierOperationsOutcome.GetAndRemoveOutcome.MISS));
        GET_AND_REMOVE_TRANSLATION = Collections.unmodifiableMap(translation3);
        Map<GetOutcome, Set<CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome>> translation4 = new EnumMap<>(GetOutcome.class);
        translation4.put(GetOutcome.HIT, EnumSet.of(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.HIT));
        translation4.put(GetOutcome.MISS, EnumSet.of(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.FAULTED, CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.FAULT_FAILED, CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.FAULT_FAILED_MISS, CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.MISS));
        GET_OR_COMPUTEIFABSENT_TRANSLATION = Collections.unmodifiableMap(translation4);
        Map<EvictionOutcome, Set<StoreOperationOutcomes.EvictionOutcome>> translation5 = new EnumMap<>(EvictionOutcome.class);
        translation5.put(EvictionOutcome.SUCCESS, EnumSet.of(StoreOperationOutcomes.EvictionOutcome.SUCCESS));
        translation5.put(EvictionOutcome.FAILURE, EnumSet.of(StoreOperationOutcomes.EvictionOutcome.FAILURE));
        EVICTION_TRANSLATION = Collections.unmodifiableMap(translation5);
    }
}
