package org.terracotta.statistics.extended;

import java.lang.Enum;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.terracotta.statistics.OperationStatistic;
import org.terracotta.statistics.ValueStatistic;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/CompoundOperationImpl.class */
public class CompoundOperationImpl<T extends Enum<T>> implements CompoundOperation<T> {
    private final OperationStatistic<T> source;
    private final Class<T> type;
    private final Map<T, ResultImpl<T>> operations;
    private final ScheduledExecutorService executor;
    private volatile long averagePeriod;
    private volatile TimeUnit averageTimeUnit;
    private volatile int historySize;
    private volatile long historyPeriod;
    private volatile TimeUnit historyTimeUnit;
    private final ConcurrentMap<Set<T>, ResultImpl<T>> compounds = new ConcurrentHashMap();
    private final ConcurrentMap<List<Set<T>>, ExpiringSampledStatistic<Double>> ratios = new ConcurrentHashMap();
    private volatile boolean alwaysOn = false;

    public CompoundOperationImpl(OperationStatistic<T> source, Class<T> type, long averagePeriod, TimeUnit averageTimeUnit, ScheduledExecutorService executor, int historySize, long historyPeriod, TimeUnit historyTimeUnit) {
        this.type = type;
        this.source = source;
        this.averagePeriod = averagePeriod;
        this.averageTimeUnit = averageTimeUnit;
        this.executor = executor;
        this.historySize = historySize;
        this.historyPeriod = historyPeriod;
        this.historyTimeUnit = historyTimeUnit;
        this.operations = new EnumMap(type);
        for (T result : type.getEnumConstants()) {
            this.operations.put(result, new ResultImpl<>(source, EnumSet.of(result), averagePeriod, averageTimeUnit, executor, historySize, historyPeriod, historyTimeUnit));
        }
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public Class<T> type() {
        return this.type;
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public Result component(T result) {
        return this.operations.get(result);
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public Result compound(EnumSet<T> results) {
        if (results.size() == 1) {
            return component((Enum) results.iterator().next());
        }
        Set<T> key = EnumSet.copyOf((EnumSet) results);
        ResultImpl<T> existing = this.compounds.get(key);
        if (existing == null) {
            ResultImpl<T> created = new ResultImpl<>(this.source, key, this.averagePeriod, this.averageTimeUnit, this.executor, this.historySize, this.historyPeriod, this.historyTimeUnit);
            ResultImpl<T> racer = this.compounds.putIfAbsent(key, created);
            if (racer == null) {
                return created;
            }
            return racer;
        }
        return existing;
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public CountOperation<T> asCountOperation() {
        return new CountOperationImpl(this);
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public SampledStatistic<Double> ratioOf(EnumSet<T> numerator, EnumSet<T> denominator) {
        List<Set<T>> key = Arrays.asList(EnumSet.copyOf((EnumSet) numerator), EnumSet.copyOf((EnumSet) denominator));
        ExpiringSampledStatistic<Double> existing = this.ratios.get(key);
        if (existing == null) {
            final SampledStatistic<Double> numeratorRate = compound(numerator).rate();
            final SampledStatistic<Double> denominatorRate = compound(denominator).rate();
            ExpiringSampledStatistic<Double> created = new ExpiringSampledStatistic<>(new ValueStatistic<Double>() { // from class: org.terracotta.statistics.extended.CompoundOperationImpl.1
                @Override // org.terracotta.statistics.ValueStatistic
                public Double value() {
                    return Double.valueOf(((Double) numeratorRate.value()).doubleValue() / ((Double) denominatorRate.value()).doubleValue());
                }
            }, this.executor, this.historySize, this.historyPeriod, this.historyTimeUnit, SampleType.RATIO);
            ExpiringSampledStatistic<Double> racer = this.ratios.putIfAbsent(key, created);
            if (racer == null) {
                return created;
            }
            return racer;
        }
        return existing;
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation, org.terracotta.statistics.extended.SamplingSupport
    public void setAlwaysOn(boolean enable) {
        this.alwaysOn = enable;
        if (enable) {
            for (ResultImpl<T> op : this.operations.values()) {
                op.start();
            }
            for (ResultImpl<T> op2 : this.compounds.values()) {
                op2.start();
            }
            for (ExpiringSampledStatistic<Double> ratio : this.ratios.values()) {
                ratio.start();
            }
        }
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public boolean isAlwaysOn() {
        return this.alwaysOn;
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public void setWindow(long time, TimeUnit unit) {
        this.averagePeriod = time;
        this.averageTimeUnit = unit;
        for (ResultImpl<T> op : this.operations.values()) {
            op.setWindow(this.averagePeriod, this.averageTimeUnit);
        }
        for (ResultImpl<T> op2 : this.compounds.values()) {
            op2.setWindow(this.averagePeriod, this.averageTimeUnit);
        }
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public void setHistory(int samples, long time, TimeUnit unit) {
        this.historySize = samples;
        this.historyPeriod = time;
        this.historyTimeUnit = unit;
        for (ResultImpl<T> op : this.operations.values()) {
            op.setHistory(this.historySize, this.historyPeriod, this.historyTimeUnit);
        }
        for (ResultImpl<T> op2 : this.compounds.values()) {
            op2.setHistory(this.historySize, this.historyPeriod, this.historyTimeUnit);
        }
        for (ExpiringSampledStatistic<Double> ratio : this.ratios.values()) {
            ratio.setHistory(this.historySize, this.historyPeriod, this.historyTimeUnit);
        }
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public long getWindowSize(TimeUnit unit) {
        return unit.convert(this.averagePeriod, unit);
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public int getHistorySampleSize() {
        return this.historySize;
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public long getHistorySampleTime(TimeUnit unit) {
        return unit.convert(this.historySize, unit);
    }

    @Override // org.terracotta.statistics.extended.SamplingSupport
    public boolean expire(long expiryTime) {
        if (this.alwaysOn) {
            return false;
        }
        boolean expired = true;
        Iterator i$ = this.operations.values().iterator();
        while (i$.hasNext()) {
            expired &= i$.next().expire(expiryTime);
        }
        Iterator<ResultImpl<T>> it = this.compounds.values().iterator();
        while (it.hasNext()) {
            if (it.next().expire(expiryTime)) {
                it.remove();
            }
        }
        Iterator<ExpiringSampledStatistic<Double>> it2 = this.ratios.values().iterator();
        while (it2.hasNext()) {
            if (it2.next().expire(expiryTime)) {
                it2.remove();
            }
        }
        return expired & this.compounds.isEmpty() & this.ratios.isEmpty();
    }
}
