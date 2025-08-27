package org.terracotta.statistics.extended;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/Result.class */
public interface Result {
    SampledStatistic<Long> count();

    SampledStatistic<Double> rate();

    Latency latency();
}
