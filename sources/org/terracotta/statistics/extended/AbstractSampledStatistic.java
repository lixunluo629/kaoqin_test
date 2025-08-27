package org.terracotta.statistics.extended;

import java.lang.Number;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.terracotta.statistics.ValueStatistic;
import org.terracotta.statistics.archive.Timestamped;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/AbstractSampledStatistic.class */
abstract class AbstractSampledStatistic<T extends Number> implements SampledStatistic<T> {
    private final ValueStatistic<T> source;
    private final StatisticHistory<T> history;
    private final SampleType type;

    AbstractSampledStatistic(ValueStatistic<T> source, ScheduledExecutorService executor, int historySize, long historyPeriod, TimeUnit historyTimeUnit, SampleType type) {
        this.source = source;
        this.type = type;
        this.history = new StatisticHistory<>(source, executor, historySize, historyPeriod, historyTimeUnit);
    }

    @Override // org.terracotta.statistics.extended.SampledStatistic
    public T value() {
        return (T) this.source.value();
    }

    @Override // org.terracotta.statistics.extended.SampledStatistic
    public List<Timestamped<T>> history() {
        return this.history.history();
    }

    @Override // org.terracotta.statistics.extended.SampledStatistic
    public List<Timestamped<T>> history(long since) {
        return this.history.history(since);
    }

    @Override // org.terracotta.statistics.extended.SampledStatistic
    public SampleType type() {
        return this.type;
    }

    final void startSampling() {
        this.history.startSampling();
    }

    final void stopSampling() {
        this.history.stopSampling();
    }

    final void setHistory(int historySize, long historyPeriod, TimeUnit historyTimeUnit) {
        this.history.adjust(historySize, historyPeriod, historyTimeUnit);
    }
}
