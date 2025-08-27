package org.terracotta.statistics.extended;

import java.lang.Number;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.terracotta.statistics.ValueStatistic;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/ExpiringSampledStatistic.class */
public class ExpiringSampledStatistic<T extends Number> extends SemiExpiringSampledStatistic<T> {
    public ExpiringSampledStatistic(ValueStatistic<T> source, ScheduledExecutorService executor, int historySize, long historyPeriod, TimeUnit historyTimeUnit, SampleType type) {
        super(source, executor, historySize, historyPeriod, historyTimeUnit, type);
    }

    @Override // org.terracotta.statistics.extended.SemiExpiringSampledStatistic, org.terracotta.statistics.extended.AbstractSampledStatistic, org.terracotta.statistics.extended.SampledStatistic
    public T value() {
        touch();
        return (T) super.value();
    }
}
