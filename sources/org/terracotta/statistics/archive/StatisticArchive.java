package org.terracotta.statistics.archive;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.terracotta.statistics.archive.StatisticSampler;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/archive/StatisticArchive.class */
public class StatisticArchive<T> implements SampleSink<Timestamped<T>> {
    private static final Comparator<Timestamped<?>> TIMESTAMPED_COMPARATOR = new Comparator<Timestamped<?>>() { // from class: org.terracotta.statistics.archive.StatisticArchive.1
        @Override // java.util.Comparator
        public int compare(Timestamped<?> o1, Timestamped<?> o2) {
            if (o1.getTimestamp() == o2.getTimestamp()) {
                return 0;
            }
            return o1.getTimestamp() < o2.getTimestamp() ? -1 : 1;
        }
    };
    private final SampleSink<? super Timestamped<T>> overspill;
    private volatile int size;
    private volatile CircularBuffer<Timestamped<T>> buffer;

    public StatisticArchive(int size) {
        this(size, DevNull.DEV_NULL);
    }

    public StatisticArchive(int size, SampleSink<? super Timestamped<T>> overspill) {
        this.size = size;
        this.overspill = overspill;
    }

    public synchronized void setCapacity(int samples) {
        if (samples != this.size) {
            this.size = samples;
            if (this.buffer != null) {
                CircularBuffer<Timestamped<T>> newBuffer = new CircularBuffer<>(this.size);
                for (Timestamped<T> sample : getArchive()) {
                    this.overspill.accept(newBuffer.insert(sample));
                }
                this.buffer = newBuffer;
            }
        }
    }

    @Override // org.terracotta.statistics.archive.SampleSink
    public synchronized void accept(Timestamped<T> object) {
        if (this.buffer == null) {
            this.buffer = new CircularBuffer<>(this.size);
        }
        this.overspill.accept(this.buffer.insert(object));
    }

    public synchronized void clear() {
        this.buffer = null;
    }

    public List<Timestamped<T>> getArchive() {
        CircularBuffer<Timestamped<T>> read = this.buffer;
        if (read == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Arrays.asList((Timestamped[]) read.toArray(Timestamped[].class)));
    }

    public List<Timestamped<T>> getArchive(long since) {
        CircularBuffer<Timestamped<T>> read = this.buffer;
        if (read == null) {
            return Collections.emptyList();
        }
        Timestamped<T> e = new StatisticSampler.Sample<>(since, null);
        Timestamped<T>[] array = (Timestamped[]) read.toArray(Timestamped[].class);
        int pos = Arrays.binarySearch(array, e, TIMESTAMPED_COMPARATOR);
        if (pos < 0) {
            pos = (-pos) - 1;
        }
        if (pos >= array.length) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Arrays.asList(Arrays.copyOfRange(array, pos, array.length)));
    }
}
