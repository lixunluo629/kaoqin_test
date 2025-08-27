package org.terracotta.statistics.extended;

import java.lang.Number;
import java.util.List;
import org.terracotta.statistics.archive.Timestamped;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/SampledStatistic.class */
public interface SampledStatistic<T extends Number> {
    boolean active();

    T value();

    List<Timestamped<T>> history();

    List<Timestamped<T>> history(long j);

    SampleType type();
}
