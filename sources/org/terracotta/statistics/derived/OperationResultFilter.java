package org.terracotta.statistics.derived;

import java.lang.Enum;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import org.terracotta.statistics.AbstractSourceStatistic;
import org.terracotta.statistics.observer.ChainedEventObserver;
import org.terracotta.statistics.observer.ChainedOperationObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/derived/OperationResultFilter.class */
public class OperationResultFilter<T extends Enum<T>> extends AbstractSourceStatistic<ChainedEventObserver> implements ChainedOperationObserver<T> {
    private final Set<T> targets;

    public OperationResultFilter(Set<T> targets, ChainedEventObserver... observers) {
        this.targets = EnumSet.copyOf((Collection) targets);
        for (ChainedEventObserver observer : observers) {
            addDerivedStatistic(observer);
        }
    }

    @Override // org.terracotta.statistics.observer.ChainedOperationObserver
    public void begin(long time) {
    }

    @Override // org.terracotta.statistics.observer.ChainedOperationObserver
    public void end(long time, T result) {
        if (!this.derivedStatistics.isEmpty() && this.targets.contains(result)) {
            for (T derived : this.derivedStatistics) {
                derived.event(time, new long[0]);
            }
        }
    }

    @Override // org.terracotta.statistics.observer.ChainedOperationObserver
    public void end(long time, T result, long... parameters) {
        if (!this.derivedStatistics.isEmpty() && this.targets.contains(result)) {
            for (T derived : this.derivedStatistics) {
                derived.event(time, parameters);
            }
        }
    }
}
