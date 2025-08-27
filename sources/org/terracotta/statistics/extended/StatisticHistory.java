package org.terracotta.statistics.extended;

import java.lang.Number;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.terracotta.statistics.ValueStatistic;
import org.terracotta.statistics.archive.StatisticArchive;
import org.terracotta.statistics.archive.StatisticSampler;
import org.terracotta.statistics.archive.Timestamped;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/StatisticHistory.class */
class StatisticHistory<T extends Number> {
    private final StatisticSampler<T> sampler;
    private final StatisticArchive<T> history;

    public StatisticHistory(ValueStatistic<T> statistic, ScheduledExecutorService executor, int historySize, long period, TimeUnit periodTimeUnit) {
        this.history = new StatisticArchive<>(historySize);
        this.sampler = new StatisticSampler<>(executor, period, periodTimeUnit, statistic, this.history);
    }

    public void startSampling() {
        this.sampler.start();
    }

    public void stopSampling() {
        this.sampler.stop();
        this.history.clear();
    }

    public List<Timestamped<T>> history() {
        return this.history.getArchive();
    }

    public List<Timestamped<T>> history(long since) {
        return this.history.getArchive(since);
    }

    void adjust(int historySize, long historyPeriod, TimeUnit historyTimeUnit) {
        this.history.setCapacity(historySize);
        this.sampler.setPeriod(historyPeriod, historyTimeUnit);
    }
}
