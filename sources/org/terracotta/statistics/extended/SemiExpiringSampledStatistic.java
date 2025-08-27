package org.terracotta.statistics.extended;

import java.lang.Number;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.terracotta.statistics.Time;
import org.terracotta.statistics.ValueStatistic;
import org.terracotta.statistics.archive.Timestamped;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/SemiExpiringSampledStatistic.class */
public class SemiExpiringSampledStatistic<T extends Number> extends AbstractSampledStatistic<T> implements SamplingSupport {
    private boolean active;
    private long touchTimestamp;
    private volatile boolean alwaysOn;

    @Override // org.terracotta.statistics.extended.AbstractSampledStatistic, org.terracotta.statistics.extended.SampledStatistic
    public /* bridge */ /* synthetic */ SampleType type() {
        return super.type();
    }

    @Override // org.terracotta.statistics.extended.AbstractSampledStatistic, org.terracotta.statistics.extended.SampledStatistic
    public /* bridge */ /* synthetic */ Number value() {
        return super.value();
    }

    public SemiExpiringSampledStatistic(ValueStatistic<T> source, ScheduledExecutorService executor, int historySize, long historyTime, TimeUnit historyTimeUnit, SampleType type) {
        super(source, executor, historySize, historyTime, historyTimeUnit, type);
        this.active = false;
        this.touchTimestamp = -1L;
        this.alwaysOn = false;
    }

    @Override // org.terracotta.statistics.extended.AbstractSampledStatistic, org.terracotta.statistics.extended.SampledStatistic
    public List<Timestamped<T>> history() {
        touch();
        return super.history();
    }

    @Override // org.terracotta.statistics.extended.AbstractSampledStatistic, org.terracotta.statistics.extended.SampledStatistic
    public List<Timestamped<T>> history(long since) {
        touch();
        return super.history(since);
    }

    @Override // org.terracotta.statistics.extended.SampledStatistic
    public final synchronized boolean active() {
        return this.active;
    }

    protected final synchronized void touch() {
        this.touchTimestamp = Time.absoluteTime();
        start();
    }

    protected final synchronized void start() {
        if (!this.active) {
            startStatistic();
            startSampling();
            this.active = true;
        }
    }

    @Override // org.terracotta.statistics.extended.SamplingSupport
    public final synchronized boolean expire(long expiry) {
        if (!this.alwaysOn && this.touchTimestamp < expiry) {
            if (this.active) {
                stopSampling();
                stopStatistic();
                this.active = false;
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // org.terracotta.statistics.extended.SamplingSupport
    public void setAlwaysOn(boolean enable) {
        this.alwaysOn = enable;
        if (enable) {
            start();
        }
    }

    protected void stopStatistic() {
    }

    protected void startStatistic() {
    }
}
