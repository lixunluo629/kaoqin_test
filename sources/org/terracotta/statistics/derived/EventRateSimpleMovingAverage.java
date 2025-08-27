package org.terracotta.statistics.derived;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.terracotta.statistics.Time;
import org.terracotta.statistics.ValueStatistic;
import org.terracotta.statistics.jsr166e.LongAdder;
import org.terracotta.statistics.observer.ChainedEventObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/derived/EventRateSimpleMovingAverage.class */
public class EventRateSimpleMovingAverage implements ChainedEventObserver, ValueStatistic<Double> {
    private static final int PARTITION_COUNT = 10;
    private final Queue<CounterPartition> archive = new ConcurrentLinkedQueue();
    private final AtomicReference<CounterPartition> activePartition;
    private volatile long windowSize;
    private volatile long partitionSize;

    public EventRateSimpleMovingAverage(long time, TimeUnit unit) {
        this.windowSize = unit.toNanos(time);
        this.partitionSize = this.windowSize / 10;
        this.activePartition = new AtomicReference<>(new CounterPartition(Time.time(), this.partitionSize));
    }

    public void setWindow(long time, TimeUnit unit) {
        this.windowSize = unit.toNanos(time);
        this.partitionSize = this.windowSize / 10;
    }

    @Override // org.terracotta.statistics.ValueStatistic
    public Double value() {
        return rateUsingSeconds();
    }

    public Double rateUsingSeconds() {
        long count;
        CounterPartition partition;
        long endTime = Time.time();
        long startTime = endTime - this.windowSize;
        CounterPartition current = this.activePartition.get();
        long actualStartTime = startTime;
        if (current.isBefore(startTime)) {
            count = 0;
        } else {
            count = current.sum();
            actualStartTime = Math.min(actualStartTime, current.start());
        }
        Iterator<CounterPartition> it = this.archive.iterator();
        while (it.hasNext() && (partition = it.next()) != current) {
            if (partition.isBefore(startTime)) {
                it.remove();
            } else {
                actualStartTime = Math.min(actualStartTime, partition.start());
                count += partition.sum();
            }
        }
        if (count == 0) {
            return Double.valueOf(0.0d);
        }
        return Double.valueOf((TimeUnit.SECONDS.toNanos(1L) * count) / (endTime - actualStartTime));
    }

    public Double rate(TimeUnit base) {
        return Double.valueOf(rateUsingSeconds().doubleValue() * (base.toNanos(1L) / TimeUnit.SECONDS.toNanos(1L)));
    }

    @Override // org.terracotta.statistics.observer.ChainedEventObserver
    public void event(long time, long... parameters) {
        CounterPartition partition;
        CounterPartition newPartition;
        do {
            partition = this.activePartition.get();
            if (partition.targetFor(time)) {
                partition.increment();
                return;
            }
            newPartition = new CounterPartition(time, this.partitionSize);
        } while (!this.activePartition.compareAndSet(partition, newPartition));
        archive(partition);
        newPartition.increment();
    }

    private void archive(CounterPartition partition) {
        this.archive.add(partition);
        long startTime = partition.end() - this.windowSize;
        CounterPartition counterPartitionPeek = this.archive.peek();
        while (true) {
            CounterPartition earliest = counterPartitionPeek;
            if (earliest != null && earliest.isBefore(startTime) && !this.archive.remove(earliest)) {
                counterPartitionPeek = this.archive.peek();
            } else {
                return;
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/derived/EventRateSimpleMovingAverage$CounterPartition.class */
    static class CounterPartition extends LongAdder {
        private final long start;
        private final long end;

        CounterPartition(long start, long length) {
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
    }
}
