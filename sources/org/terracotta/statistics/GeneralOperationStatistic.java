package org.terracotta.statistics;

import java.lang.Enum;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import org.terracotta.statistics.jsr166e.LongAdder;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/GeneralOperationStatistic.class */
class GeneralOperationStatistic<T extends Enum<T>> extends AbstractOperationStatistic<T> implements OperationStatistic<T> {
    private final EnumMap<T, LongAdder> counts;

    GeneralOperationStatistic(String name, Set<String> tags, Map<String, ? extends Object> properties, Class<T> type) {
        super(name, tags, properties, type);
        this.counts = new EnumMap<>(type);
        for (T t : type.getEnumConstants()) {
            this.counts.put((EnumMap<T, LongAdder>) t, (T) new LongAdder());
        }
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public long count(T type) {
        return this.counts.get(type).sum();
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public long sum(Set<T> types) {
        long sum = 0;
        for (T t : types) {
            sum += this.counts.get(t).sum();
        }
        return sum;
    }

    @Override // org.terracotta.statistics.observer.OperationObserver
    public void end(T result) {
        this.counts.get(result).increment();
        if (!this.derivedStatistics.isEmpty()) {
            long time = Time.time();
            for (T observer : this.derivedStatistics) {
                observer.end(time, result);
            }
        }
    }

    @Override // org.terracotta.statistics.observer.OperationObserver
    public void end(T result, long... parameters) {
        this.counts.get(result).increment();
        if (!this.derivedStatistics.isEmpty()) {
            long time = Time.time();
            for (T observer : this.derivedStatistics) {
                observer.end(time, result, parameters);
            }
        }
    }

    public String toString() {
        return this.counts.toString();
    }
}
