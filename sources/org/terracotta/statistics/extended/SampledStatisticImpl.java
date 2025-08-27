package org.terracotta.statistics.extended;

import java.lang.Number;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.terracotta.statistics.ValueStatistic;
import org.terracotta.statistics.archive.Timestamped;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/SampledStatisticImpl.class */
class SampledStatisticImpl<T extends Number> extends AbstractSampledStatistic<T> {
    private final LatencyImpl latency;

    public SampledStatisticImpl(LatencyImpl latency, ValueStatistic<T> value, ScheduledExecutorService executor, int historySize, long historyPeriod, TimeUnit historyTimeUnit, SampleType type) {
        super(value, executor, historySize, historyPeriod, historyTimeUnit, type);
        this.latency = latency;
    }

    @Override // org.terracotta.statistics.extended.SampledStatistic
    public boolean active() {
        return this.latency.active();
    }

    @Override // org.terracotta.statistics.extended.AbstractSampledStatistic, org.terracotta.statistics.extended.SampledStatistic
    public T value() {
        this.latency.touch();
        return (T) super.value();
    }

    @Override // org.terracotta.statistics.extended.AbstractSampledStatistic, org.terracotta.statistics.extended.SampledStatistic
    public List<Timestamped<T>> history() {
        this.latency.touch();
        return super.history();
    }

    @Override // org.terracotta.statistics.extended.AbstractSampledStatistic, org.terracotta.statistics.extended.SampledStatistic
    public List<Timestamped<T>> history(long since) {
        this.latency.touch();
        return super.history(since);
    }
}
