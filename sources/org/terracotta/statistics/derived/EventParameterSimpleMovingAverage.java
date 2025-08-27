package org.terracotta.statistics.derived;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.terracotta.statistics.Time;
import org.terracotta.statistics.ValueStatistic;
import org.terracotta.statistics.jsr166e.LongAdder;
import org.terracotta.statistics.jsr166e.LongMaxUpdater;
import org.terracotta.statistics.observer.ChainedEventObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/derived/EventParameterSimpleMovingAverage.class */
public class EventParameterSimpleMovingAverage implements ChainedEventObserver {
    private static final int PARTITION_COUNT = 10;
    private final Queue<AveragePartition> archive = new ConcurrentLinkedQueue();
    private final AtomicReference<AveragePartition> activePartition;
    private volatile long windowSize;
    private volatile long partitionSize;

    public EventParameterSimpleMovingAverage(long time, TimeUnit unit) {
        this.windowSize = unit.toNanos(time);
        this.partitionSize = this.windowSize / 10;
        this.activePartition = new AtomicReference<>(new AveragePartition(Long.MIN_VALUE, this.partitionSize));
    }

    public void setWindow(long time, TimeUnit unit) {
        this.windowSize = unit.toNanos(time);
        this.partitionSize = this.windowSize / 10;
    }

    public Double value() {
        return Double.valueOf(average());
    }

    public ValueStatistic<Double> averageStatistic() {
        return new ValueStatistic<Double>() { // from class: org.terracotta.statistics.derived.EventParameterSimpleMovingAverage.1
            @Override // org.terracotta.statistics.ValueStatistic
            public Double value() {
                return Double.valueOf(EventParameterSimpleMovingAverage.this.average());
            }
        };
    }

    public ValueStatistic<Long> minimumStatistic() {
        return new ValueStatistic<Long>() { // from class: org.terracotta.statistics.derived.EventParameterSimpleMovingAverage.2
            @Override // org.terracotta.statistics.ValueStatistic
            public Long value() {
                return EventParameterSimpleMovingAverage.this.minimum();
            }
        };
    }

    public ValueStatistic<Long> maximumStatistic() {
        return new ValueStatistic<Long>() { // from class: org.terracotta.statistics.derived.EventParameterSimpleMovingAverage.3
            @Override // org.terracotta.statistics.ValueStatistic
            public Long value() {
                return EventParameterSimpleMovingAverage.this.maximum();
            }
        };
    }

    public final double average() {
        AveragePartition partition;
        long startTime = Time.time() - this.windowSize;
        AveragePartition current = this.activePartition.get();
        if (current.isBefore(startTime)) {
            return Double.NaN;
        }
        Average average = new Average();
        current.aggregate(average);
        Iterator<AveragePartition> it = this.archive.iterator();
        while (it.hasNext() && (partition = it.next()) != current) {
            if (partition.isBefore(startTime)) {
                it.remove();
            } else {
                partition.aggregate(average);
            }
        }
        return average.total / average.count;
    }

    public final Long maximum() {
        AveragePartition partition;
        long startTime = Time.time() - this.windowSize;
        AveragePartition current = this.activePartition.get();
        if (current.isBefore(startTime)) {
            return null;
        }
        long maximum = current.maximum();
        Iterator<AveragePartition> it = this.archive.iterator();
        while (it.hasNext() && (partition = it.next()) != current) {
            if (partition.isBefore(startTime)) {
                it.remove();
            } else {
                maximum = Math.max(maximum, partition.maximum());
            }
        }
        return Long.valueOf(maximum);
    }

    public final Long minimum() {
        AveragePartition partition;
        long startTime = Time.time() - this.windowSize;
        AveragePartition current = this.activePartition.get();
        if (current.isBefore(startTime)) {
            return null;
        }
        long minimum = current.minimum();
        Iterator<AveragePartition> it = this.archive.iterator();
        while (it.hasNext() && (partition = it.next()) != current) {
            if (partition.isBefore(startTime)) {
                it.remove();
            } else {
                minimum = Math.min(minimum, partition.minimum());
            }
        }
        return Long.valueOf(minimum);
    }

    @Override // org.terracotta.statistics.observer.ChainedEventObserver
    public void event(long time, long... parameters) {
        AveragePartition partition;
        AveragePartition newPartition;
        do {
            partition = this.activePartition.get();
            if (partition.targetFor(time)) {
                partition.event(parameters[0]);
                return;
            }
            newPartition = new AveragePartition(time, this.partitionSize);
        } while (!this.activePartition.compareAndSet(partition, newPartition));
        archive(partition);
        newPartition.event(parameters[0]);
    }

    private void archive(AveragePartition partition) {
        this.archive.add(partition);
        long startTime = partition.end() - this.windowSize;
        AveragePartition averagePartitionPeek = this.archive.peek();
        while (true) {
            AveragePartition earliest = averagePartitionPeek;
            if (earliest != null && earliest.isBefore(startTime) && !this.archive.remove(earliest)) {
                averagePartitionPeek = this.archive.peek();
            } else {
                return;
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/derived/EventParameterSimpleMovingAverage$AveragePartition.class */
    static class AveragePartition {
        private final LongAdder total = new LongAdder();
        private final LongAdder count = new LongAdder();
        private final LongMaxUpdater maximum = new LongMaxUpdater();
        private final LongMaxUpdater minimum = new LongMaxUpdater();
        private final long start;
        private final long end;

        public AveragePartition(long start, long length) {
            this.start = start;
            this.end = start + length;
        }

        public boolean targetFor(long time) {
            return this.end > time;
        }

        public boolean isBefore(long time) {
            return this.end < time;
        }

        public long start() {
            return this.start;
        }

        public long end() {
            return this.end;
        }

        public void event(long parameter) {
            this.total.add(parameter);
            this.count.increment();
            this.maximum.update(parameter);
            this.minimum.update(-parameter);
        }

        public void aggregate(Average average) {
            average.total += this.total.sum();
            average.count += this.count.sum();
        }

        public long maximum() {
            return this.maximum.max();
        }

        public long minimum() {
            return -this.minimum.max();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/derived/EventParameterSimpleMovingAverage$Average.class */
    static class Average {
        long total;
        long count;

        Average() {
        }
    }
}
