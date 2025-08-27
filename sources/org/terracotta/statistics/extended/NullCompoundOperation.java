package org.terracotta.statistics.extended;

import java.lang.Enum;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.terracotta.statistics.archive.Timestamped;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/NullCompoundOperation.class */
public final class NullCompoundOperation<T extends Enum<T>> implements CompoundOperation<T> {
    private static final CompoundOperation INSTANCE = new NullCompoundOperation();

    private NullCompoundOperation() {
    }

    public static <T extends Enum<T>> CompoundOperation<T> instance(Class<T> klazz) {
        return INSTANCE;
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public Class<T> type() {
        return null;
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public Result component(T result) {
        return NullOperation.instance();
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public Result compound(EnumSet<T> results) {
        return NullOperation.instance();
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public CountOperation<T> asCountOperation() {
        return (CountOperation<T>) new CountOperation<T>() { // from class: org.terracotta.statistics.extended.NullCompoundOperation.1
            @Override // org.terracotta.statistics.extended.CountOperation
            public long value(T result) {
                return -1L;
            }

            @Override // org.terracotta.statistics.extended.CountOperation
            public long value(T... results) {
                return -1L;
            }
        };
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public SampledStatistic<Double> ratioOf(EnumSet<T> numerator, EnumSet<T> denominator) {
        return NullSampledStatistic.instance(SampleType.RATIO);
    }

    @Override // org.terracotta.statistics.extended.SamplingSupport
    public boolean expire(long expiry) {
        return false;
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation, org.terracotta.statistics.extended.SamplingSupport
    public void setAlwaysOn(boolean enable) {
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public void setWindow(long time, TimeUnit unit) {
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public void setHistory(int samples, long time, TimeUnit unit) {
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public boolean isAlwaysOn() {
        return false;
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public long getWindowSize(TimeUnit unit) {
        return 0L;
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public int getHistorySampleSize() {
        return 0;
    }

    @Override // org.terracotta.statistics.extended.CompoundOperation
    public long getHistorySampleTime(TimeUnit unit) {
        return 0L;
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/NullCompoundOperation$NullOperation.class */
    static final class NullOperation implements Result {
        private static final Result INSTANCE = new NullOperation();

        private NullOperation() {
        }

        static final Result instance() {
            return INSTANCE;
        }

        @Override // org.terracotta.statistics.extended.Result
        public SampledStatistic<Long> count() {
            return NullSampledStatistic.instance(SampleType.COUNTER);
        }

        @Override // org.terracotta.statistics.extended.Result
        public SampledStatistic<Double> rate() {
            return NullSampledStatistic.instance(SampleType.RATE);
        }

        @Override // org.terracotta.statistics.extended.Result
        public Latency latency() throws UnsupportedOperationException {
            return NullLatency.instance();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/NullCompoundOperation$NullLatency.class */
    static final class NullLatency implements Latency {
        private static final Latency INSTANCE = new NullLatency();

        private NullLatency() {
        }

        static Latency instance() {
            return INSTANCE;
        }

        @Override // org.terracotta.statistics.extended.Latency
        public SampledStatistic<Long> minimum() {
            return NullSampledStatistic.instance(SampleType.LATENCY_MIN);
        }

        @Override // org.terracotta.statistics.extended.Latency
        public SampledStatistic<Long> maximum() {
            return NullSampledStatistic.instance(SampleType.LATENCY_MAX);
        }

        @Override // org.terracotta.statistics.extended.Latency
        public SampledStatistic<Double> average() {
            return NullSampledStatistic.instance(SampleType.LATENCY_AVG);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/NullCompoundOperation$NullSampledStatistic.class */
    static final class NullSampledStatistic<T extends Number> implements SampledStatistic<T> {
        private static final Map<SampleType, SampledStatistic<?>> COMMON = new HashMap();
        private final T value;
        private final SampleType type;

        static {
            COMMON.put(SampleType.COUNTER, new NullSampledStatistic(0L, SampleType.COUNTER));
            COMMON.put(SampleType.RATE, new NullSampledStatistic(Double.valueOf(Double.NaN), SampleType.RATE));
            COMMON.put(SampleType.LATENCY_MIN, new NullSampledStatistic(null, SampleType.LATENCY_MIN));
            COMMON.put(SampleType.LATENCY_MAX, new NullSampledStatistic(null, SampleType.LATENCY_MAX));
            COMMON.put(SampleType.LATENCY_AVG, new NullSampledStatistic(Double.valueOf(Double.NaN), SampleType.LATENCY_AVG));
            COMMON.put(SampleType.RATIO, new NullSampledStatistic(Double.valueOf(Double.NaN), SampleType.RATIO));
            COMMON.put(SampleType.SIZE, new NullSampledStatistic(0L, SampleType.SIZE));
        }

        private NullSampledStatistic(T value, SampleType type) {
            this.value = value;
            this.type = type;
        }

        @Override // org.terracotta.statistics.extended.SampledStatistic
        public boolean active() {
            return false;
        }

        @Override // org.terracotta.statistics.extended.SampledStatistic
        public T value() {
            return this.value;
        }

        @Override // org.terracotta.statistics.extended.SampledStatistic
        public SampleType type() {
            return this.type;
        }

        @Override // org.terracotta.statistics.extended.SampledStatistic
        public List<Timestamped<T>> history() throws UnsupportedOperationException {
            return Collections.emptyList();
        }

        @Override // org.terracotta.statistics.extended.SampledStatistic
        public List<Timestamped<T>> history(long since) {
            return Collections.emptyList();
        }

        static <T extends Number> SampledStatistic<T> instance(SampleType type) {
            if (type == null) {
                throw new NullPointerException();
            }
            return (SampledStatistic) COMMON.get(type);
        }
    }
}
