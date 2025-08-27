package org.terracotta.statistics.extended;

import java.lang.Enum;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/CompoundOperation.class */
public interface CompoundOperation<T extends Enum<T>> extends SamplingSupport {
    Class<T> type();

    Result component(T t);

    Result compound(EnumSet<T> enumSet);

    CountOperation<T> asCountOperation();

    SampledStatistic<Double> ratioOf(EnumSet<T> enumSet, EnumSet<T> enumSet2);

    @Override // org.terracotta.statistics.extended.SamplingSupport
    void setAlwaysOn(boolean z);

    boolean isAlwaysOn();

    void setWindow(long j, TimeUnit timeUnit);

    void setHistory(int i, long j, TimeUnit timeUnit);

    long getWindowSize(TimeUnit timeUnit);

    int getHistorySampleSize();

    long getHistorySampleTime(TimeUnit timeUnit);
}
