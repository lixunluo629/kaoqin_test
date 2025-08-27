package org.terracotta.statistics;

import java.lang.Enum;
import java.util.Set;
import org.terracotta.statistics.observer.ChainedOperationObserver;
import org.terracotta.statistics.observer.OperationObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/OperationStatistic.class */
public interface OperationStatistic<T extends Enum<T>> extends OperationObserver<T>, SourceStatistic<ChainedOperationObserver<? super T>> {
    Class<T> type();

    ValueStatistic<Long> statistic(T t);

    ValueStatistic<Long> statistic(Set<T> set);

    long count(T t);

    long sum(Set<T> set);

    long sum();
}
