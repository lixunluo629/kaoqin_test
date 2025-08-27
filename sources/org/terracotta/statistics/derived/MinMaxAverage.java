package org.terracotta.statistics.derived;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import org.terracotta.statistics.ValueStatistic;
import org.terracotta.statistics.observer.ChainedEventObserver;
import org.terracotta.statistics.util.InThreadExecutor;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/derived/MinMaxAverage.class */
public class MinMaxAverage implements ChainedEventObserver {
    private final AtomicLong maximum;
    private final AtomicLong minimum;
    private final AtomicLong summation;
    private final AtomicLong count;
    private final Executor executor;

    public MinMaxAverage() {
        this(InThreadExecutor.INSTANCE);
    }

    public MinMaxAverage(Executor executor) {
        this.maximum = new AtomicLong(Long.MIN_VALUE);
        this.minimum = new AtomicLong(Long.MAX_VALUE);
        this.summation = new AtomicLong(Double.doubleToLongBits(0.0d));
        this.count = new AtomicLong(0L);
        this.executor = executor;
    }

    @Override // org.terracotta.statistics.observer.ChainedEventObserver
    public void event(long time, final long... parameters) {
        this.executor.execute(new Runnable() { // from class: org.terracotta.statistics.derived.MinMaxAverage.1
            @Override // java.lang.Runnable
            public void run() {
                long j = MinMaxAverage.this.maximum.get();
                while (true) {
                    long max = j;
                    if (max >= parameters[0] || MinMaxAverage.this.maximum.compareAndSet(max, parameters[0])) {
                        break;
                    } else {
                        j = MinMaxAverage.this.maximum.get();
                    }
                }
                long j2 = MinMaxAverage.this.minimum.get();
                while (true) {
                    long min = j2;
                    if (min <= parameters[0] || MinMaxAverage.this.minimum.compareAndSet(min, parameters[0])) {
                        break;
                    } else {
                        j2 = MinMaxAverage.this.minimum.get();
                    }
                }
                long j3 = MinMaxAverage.this.summation.get();
                while (true) {
                    long sumBits = j3;
                    if (!MinMaxAverage.this.summation.compareAndSet(sumBits, Double.doubleToLongBits(Double.longBitsToDouble(sumBits) + parameters[0]))) {
                        j3 = MinMaxAverage.this.summation.get();
                    } else {
                        MinMaxAverage.this.count.incrementAndGet();
                        return;
                    }
                }
            }
        });
    }

    public Long min() {
        if (this.count.get() == 0) {
            return null;
        }
        return Long.valueOf(this.minimum.get());
    }

    public ValueStatistic<Long> minStatistic() {
        return new ValueStatistic<Long>() { // from class: org.terracotta.statistics.derived.MinMaxAverage.2
            @Override // org.terracotta.statistics.ValueStatistic
            public Long value() {
                return MinMaxAverage.this.min();
            }
        };
    }

    public Double mean() {
        if (this.count.get() == 0) {
            return null;
        }
        return Double.valueOf(Double.longBitsToDouble(this.summation.get()) / this.count.get());
    }

    public ValueStatistic<Double> meanStatistic() {
        return new ValueStatistic<Double>() { // from class: org.terracotta.statistics.derived.MinMaxAverage.3
            @Override // org.terracotta.statistics.ValueStatistic
            public Double value() {
                return MinMaxAverage.this.mean();
            }
        };
    }

    public Long max() {
        if (this.count.get() == 0) {
            return null;
        }
        return Long.valueOf(this.maximum.get());
    }

    public ValueStatistic<Long> maxStatistic() {
        return new ValueStatistic<Long>() { // from class: org.terracotta.statistics.derived.MinMaxAverage.4
            @Override // org.terracotta.statistics.ValueStatistic
            public Long value() {
                return MinMaxAverage.this.max();
            }
        };
    }
}
