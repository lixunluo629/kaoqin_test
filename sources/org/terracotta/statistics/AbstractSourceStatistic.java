package org.terracotta.statistics;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import org.terracotta.statistics.observer.ChainedObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/AbstractSourceStatistic.class */
public class AbstractSourceStatistic<T extends ChainedObserver> implements SourceStatistic<T> {
    protected final Collection<T> derivedStatistics = new CopyOnWriteArrayList();

    @Override // org.terracotta.statistics.SourceStatistic
    public void addDerivedStatistic(T derived) {
        this.derivedStatistics.add(derived);
    }

    @Override // org.terracotta.statistics.SourceStatistic
    public void removeDerivedStatistic(T derived) {
        this.derivedStatistics.remove(derived);
    }
}
