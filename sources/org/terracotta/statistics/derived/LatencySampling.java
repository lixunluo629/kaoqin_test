package org.terracotta.statistics.derived;

import java.lang.Enum;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.statistics.AbstractSourceStatistic;
import org.terracotta.statistics.jsr166e.ThreadLocalRandom;
import org.terracotta.statistics.observer.ChainedEventObserver;
import org.terracotta.statistics.observer.ChainedOperationObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/derived/LatencySampling.class */
public class LatencySampling<T extends Enum<T>> extends AbstractSourceStatistic<ChainedEventObserver> implements ChainedOperationObserver<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) LatencySampling.class);
    private final ThreadLocal<Long> operationStartTime = new ThreadLocal<>();
    private final Set<T> targetOperations;
    private final int ceiling;

    public LatencySampling(Set<T> targets, double sampling) {
        if (sampling > 1.0d || sampling < 0.0d) {
            throw new IllegalArgumentException();
        }
        this.ceiling = (int) (2.147483647E9d * sampling);
        this.targetOperations = EnumSet.copyOf((Collection) targets);
    }

    @Override // org.terracotta.statistics.observer.ChainedOperationObserver
    public void begin(long time) {
        if (sample()) {
            this.operationStartTime.set(Long.valueOf(time));
        }
    }

    @Override // org.terracotta.statistics.observer.ChainedOperationObserver
    public void end(long time, T result) {
        Long start;
        if (this.targetOperations.contains(result) && (start = this.operationStartTime.get()) != null) {
            long latency = time - start.longValue();
            if (!this.derivedStatistics.isEmpty()) {
                if (latency < 0) {
                    LOGGER.info("Dropping {} event with negative latency {} (possible backwards nanoTime() movement)", result, Long.valueOf(time));
                } else {
                    for (T observer : this.derivedStatistics) {
                        observer.event(time, latency);
                    }
                }
            }
        }
        this.operationStartTime.remove();
    }

    @Override // org.terracotta.statistics.observer.ChainedOperationObserver
    public void end(long time, T result, long... parameters) {
        end(time, result);
    }

    private boolean sample() {
        return ((double) this.ceiling) == 1.0d || ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE) < this.ceiling;
    }
}
