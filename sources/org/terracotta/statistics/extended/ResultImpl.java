package org.terracotta.statistics.extended;

import java.lang.Enum;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.terracotta.statistics.OperationStatistic;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/ResultImpl.class */
class ResultImpl<T extends Enum<T>> implements Result {
    private final SemiExpiringSampledStatistic<Long> count;
    private final RateImpl<T> rate;
    private final LatencyImpl<T> latency;

    public ResultImpl(OperationStatistic<T> source, Set<T> targets, long averagePeriod, TimeUnit averageTimeUnit, ScheduledExecutorService executor, int historySize, long historyPeriod, TimeUnit historyTimeUnit) {
        this.count = new SemiExpiringSampledStatistic<>(source.statistic(targets), executor, historySize, historyPeriod, historyTimeUnit, SampleType.COUNTER);
        this.latency = new LatencyImpl<>(source, targets, averagePeriod, averageTimeUnit, executor, historySize, historyPeriod, historyTimeUnit);
        this.rate = new RateImpl<>(source, targets, averagePeriod, averageTimeUnit, executor, historySize, historyPeriod, historyTimeUnit);
    }

    @Override // org.terracotta.statistics.extended.Result
    public SampledStatistic<Double> rate() {
        return this.rate;
    }

    @Override // org.terracotta.statistics.extended.Result
    public Latency latency() throws UnsupportedOperationException {
        return this.latency;
    }

    @Override // org.terracotta.statistics.extended.Result
    public SampledStatistic<Long> count() {
        return this.count;
    }

    void start() {
        this.count.start();
        this.rate.start();
        this.latency.start();
    }

    boolean expire(long expiryTime) {
        return this.count.expire(expiryTime) & this.rate.expire(expiryTime) & this.latency.expire(expiryTime);
    }

    void setWindow(long averagePeriod, TimeUnit averageTimeUnit) {
        this.rate.setWindow(averagePeriod, averageTimeUnit);
        this.latency.setWindow(averagePeriod, averageTimeUnit);
    }

    void setHistory(int historySize, long historyPeriod, TimeUnit historyTimeUnit) {
        this.count.setHistory(historySize, historyPeriod, historyTimeUnit);
        this.rate.setHistory(historySize, historyPeriod, historyTimeUnit);
        this.latency.setHistory(historySize, historyPeriod, historyTimeUnit);
    }
}
