package org.terracotta.statistics.extended;

import java.lang.Enum;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.terracotta.statistics.OperationStatistic;
import org.terracotta.statistics.archive.Timestamped;
import org.terracotta.statistics.derived.EventRateSimpleMovingAverage;
import org.terracotta.statistics.derived.OperationResultFilter;
import org.terracotta.statistics.observer.ChainedOperationObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/RateImpl.class */
public class RateImpl<T extends Enum<T>> implements SampledStatistic<Double> {
    private final ExpiringSampledStatistic<Double> delegate;
    private final EventRateSimpleMovingAverage rate;

    public RateImpl(final OperationStatistic<T> source, final Set<T> targets, long averagePeriod, TimeUnit averageTimeUnit, ScheduledExecutorService executor, int historySize, long historyPeriod, TimeUnit historyTimeUnit) {
        this.rate = new EventRateSimpleMovingAverage(averagePeriod, averageTimeUnit);
        this.delegate = new ExpiringSampledStatistic<Double>(this.rate, executor, historySize, historyPeriod, historyTimeUnit, SampleType.RATE) { // from class: org.terracotta.statistics.extended.RateImpl.1
            private final ChainedOperationObserver<T> observer;

            {
                this.observer = new OperationResultFilter(targets, RateImpl.this.rate);
            }

            @Override // org.terracotta.statistics.extended.SemiExpiringSampledStatistic
            protected void stopStatistic() {
                super.stopStatistic();
                source.removeDerivedStatistic(this.observer);
            }

            @Override // org.terracotta.statistics.extended.SemiExpiringSampledStatistic
            protected void startStatistic() {
                super.startStatistic();
                source.addDerivedStatistic(this.observer);
            }
        };
    }

    @Override // org.terracotta.statistics.extended.SampledStatistic
    public boolean active() {
        return this.delegate.active();
    }

    @Override // org.terracotta.statistics.extended.SampledStatistic
    public Double value() {
        return (Double) this.delegate.value();
    }

    @Override // org.terracotta.statistics.extended.SampledStatistic
    public List<Timestamped<Double>> history() {
        return this.delegate.history();
    }

    @Override // org.terracotta.statistics.extended.SampledStatistic
    public List<Timestamped<Double>> history(long since) {
        return this.delegate.history(since);
    }

    @Override // org.terracotta.statistics.extended.SampledStatistic
    public SampleType type() {
        return SampleType.RATE;
    }

    protected void start() {
        this.delegate.start();
    }

    protected void setWindow(long averagePeriod, TimeUnit averageTimeUnit) {
        this.rate.setWindow(averagePeriod, averageTimeUnit);
    }

    protected void setHistory(int historySize, long historyPeriod, TimeUnit historyTimeUnit) {
        this.delegate.setHistory(historySize, historyPeriod, historyTimeUnit);
    }

    protected boolean expire(long expiry) {
        return this.delegate.expire(expiry);
    }
}
