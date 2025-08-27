package org.terracotta.statistics;

import org.terracotta.statistics.observer.ChainedObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/SourceStatistic.class */
public interface SourceStatistic<T extends ChainedObserver> {
    void addDerivedStatistic(T t);

    void removeDerivedStatistic(T t);
}
