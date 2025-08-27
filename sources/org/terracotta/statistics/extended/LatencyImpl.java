package org.terracotta.statistics.extended;

import java.lang.Enum;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.terracotta.statistics.OperationStatistic;
import org.terracotta.statistics.SourceStatistic;
import org.terracotta.statistics.Time;
import org.terracotta.statistics.derived.EventParameterSimpleMovingAverage;
import org.terracotta.statistics.derived.LatencySampling;
import org.terracotta.statistics.observer.ChainedOperationObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/LatencyImpl.class */
class LatencyImpl<T extends Enum<T>> implements Latency {
    private final SourceStatistic<ChainedOperationObserver<? super T>> source;
    private final LatencySampling<T> latencySampler;
    private final EventParameterSimpleMovingAverage average;
    private final SampledStatisticImpl<Long> minimumStatistic;
    private final SampledStatisticImpl<Long> maximumStatistic;
    private final SampledStatisticImpl<Double> averageStatistic;
    private boolean active = false;
    private long touchTimestamp = -1;

    public LatencyImpl(OperationStatistic<T> statistic, Set<T> targets, long averagePeriod, TimeUnit averageTimeUnit, ScheduledExecutorService executor, int historySize, long historyPeriod, TimeUnit historyTimeUnit) {
        this.average = new EventParameterSimpleMovingAverage(averagePeriod, averageTimeUnit);
        this.minimumStatistic = new SampledStatisticImpl<>(this, this.average.minimumStatistic(), executor, historySize, historyPeriod, historyTimeUnit, SampleType.LATENCY_MIN);
        this.maximumStatistic = new SampledStatisticImpl<>(this, this.average.maximumStatistic(), executor, historySize, historyPeriod, historyTimeUnit, SampleType.LATENCY_MAX);
        this.averageStatistic = new SampledStatisticImpl<>(this, this.average.averageStatistic(), executor, historySize, historyPeriod, historyTimeUnit, SampleType.LATENCY_AVG);
        this.latencySampler = new LatencySampling<>(targets, 1.0d);
        this.latencySampler.addDerivedStatistic(this.average);
        this.source = statistic;
    }

    synchronized void start() {
        if (!this.active) {
            this.source.addDerivedStatistic(this.latencySampler);
            this.minimumStatistic.startSampling();
            this.maximumStatistic.startSampling();
            this.averageStatistic.startSampling();
            this.active = true;
        }
    }

    @Override // org.terracotta.statistics.extended.Latency
    public SampledStatistic<Long> minimum() {
        return this.minimumStatistic;
    }

    @Override // org.terracotta.statistics.extended.Latency
    public SampledStatistic<Long> maximum() {
        return this.maximumStatistic;
    }

    @Override // org.terracotta.statistics.extended.Latency
    public SampledStatistic<Double> average() {
        return this.averageStatistic;
    }

    synchronized void touch() {
        this.touchTimestamp = Time.absoluteTime();
        start();
    }

    public synchronized boolean expire(long expiry) {
        if (this.touchTimestamp < expiry) {
            if (this.active) {
                this.source.removeDerivedStatistic(this.latencySampler);
                this.minimumStatistic.stopSampling();
                this.maximumStatistic.stopSampling();
                this.averageStatistic.stopSampling();
                this.active = false;
                return true;
            }
            return true;
        }
        return false;
    }

    void setWindow(long averagePeriod, TimeUnit averageTimeUnit) {
        this.average.setWindow(averagePeriod, averageTimeUnit);
    }

    void setHistory(int historySize, long historyPeriod, TimeUnit historyTimeUnit) {
        this.minimumStatistic.setHistory(historySize, historyPeriod, historyTimeUnit);
        this.maximumStatistic.setHistory(historySize, historyPeriod, historyTimeUnit);
        this.averageStatistic.setHistory(historySize, historyPeriod, historyTimeUnit);
    }

    public boolean active() {
        return this.active;
    }
}
