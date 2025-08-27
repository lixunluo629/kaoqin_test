package org.terracotta.statistics.archive;

import java.lang.Number;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.terracotta.statistics.Time;
import org.terracotta.statistics.ValueStatistic;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/archive/StatisticSampler.class */
public class StatisticSampler<T extends Number> {
    private final boolean exclusiveExecutor;
    private final ScheduledExecutorService executor;
    private final Runnable task;
    private ScheduledFuture<?> currentExecution;
    private long period;

    public StatisticSampler(long time, TimeUnit unit, ValueStatistic<T> statistic, SampleSink<? super Timestamped<T>> sink) {
        this(null, time, unit, statistic, sink);
    }

    public StatisticSampler(ScheduledExecutorService executor, long time, TimeUnit unit, ValueStatistic<T> statistic, SampleSink<? super Timestamped<T>> sink) {
        if (executor == null) {
            this.exclusiveExecutor = true;
            this.executor = Executors.newSingleThreadScheduledExecutor(new SamplerThreadFactory());
        } else {
            this.exclusiveExecutor = false;
            this.executor = executor;
        }
        this.period = unit.toNanos(time);
        this.task = new SamplingTask(statistic, sink);
    }

    public synchronized void setPeriod(long time, TimeUnit unit) {
        this.period = unit.toNanos(time);
        if (this.currentExecution != null && !this.currentExecution.isDone()) {
            stop();
            start();
        }
    }

    public synchronized void start() {
        if (this.currentExecution == null || this.currentExecution.isDone()) {
            this.currentExecution = this.executor.scheduleAtFixedRate(this.task, this.period, this.period, TimeUnit.NANOSECONDS);
            return;
        }
        throw new IllegalStateException("Sampler is already running");
    }

    public synchronized void stop() {
        if (this.currentExecution == null || this.currentExecution.isDone()) {
            throw new IllegalStateException("Sampler is not running");
        }
        this.currentExecution.cancel(false);
    }

    public synchronized void shutdown() throws InterruptedException {
        if (this.exclusiveExecutor) {
            this.executor.shutdown();
            if (!this.executor.awaitTermination(10L, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Exclusive ScheduledExecutorService failed to terminate promptly");
            }
            return;
        }
        throw new IllegalStateException("ScheduledExecutorService was supplied externally - it must be shutdown directly");
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/archive/StatisticSampler$SamplingTask.class */
    static class SamplingTask<T extends Number> implements Runnable {
        private final ValueStatistic<T> statistic;
        private final SampleSink<Timestamped<T>> sink;

        SamplingTask(ValueStatistic<T> statistic, SampleSink<Timestamped<T>> sink) {
            this.statistic = statistic;
            this.sink = sink;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.sink.accept(new Sample(Time.absoluteTime(), this.statistic.value()));
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/archive/StatisticSampler$Sample.class */
    static class Sample<T> implements Timestamped<T> {
        private final T sample;
        private final long timestamp;

        public Sample(long timestamp, T sample) {
            this.sample = sample;
            this.timestamp = timestamp;
        }

        @Override // org.terracotta.statistics.archive.Timestamped
        public T getSample() {
            return this.sample;
        }

        @Override // org.terracotta.statistics.archive.Timestamped
        public long getTimestamp() {
            return this.timestamp;
        }

        public String toString() {
            return getSample() + " @ " + new Date(getTimestamp());
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/archive/StatisticSampler$SamplerThreadFactory.class */
    static class SamplerThreadFactory implements ThreadFactory {
        SamplerThreadFactory() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Statistic Sampler");
            t.setDaemon(true);
            return t;
        }
    }
}
