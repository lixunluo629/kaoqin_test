package org.terracotta.statistics.extended;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/Latency.class */
public interface Latency {
    SampledStatistic<Long> minimum();

    SampledStatistic<Long> maximum();

    SampledStatistic<Double> average();
}
