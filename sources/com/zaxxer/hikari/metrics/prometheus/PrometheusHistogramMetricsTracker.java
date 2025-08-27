package com.zaxxer.hikari.metrics.prometheus;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/metrics/prometheus/PrometheusHistogramMetricsTracker.class */
class PrometheusHistogramMetricsTracker implements IMetricsTracker {
    private static final Counter CONNECTION_TIMEOUT_COUNTER = Counter.build().name("hikaricp_connection_timeout_total").labelNames(new String[]{BaseObjectPoolConfig.DEFAULT_JMX_NAME_PREFIX}).help("Connection timeout total count").create();
    private static final Histogram ELAPSED_ACQUIRED_HISTOGRAM = registerHistogram("hikaricp_connection_acquired_nanos", "Connection acquired time (ns)", 1000.0d);
    private static final Histogram ELAPSED_BORROWED_HISTOGRAM = registerHistogram("hikaricp_connection_usage_millis", "Connection usage (ms)", 1.0d);
    private static final Histogram ELAPSED_CREATION_HISTOGRAM = registerHistogram("hikaricp_connection_creation_millis", "Connection creation (ms)", 1.0d);
    private final Counter.Child connectionTimeoutCounterChild;
    private final Histogram.Child elapsedAcquiredHistogramChild;
    private final Histogram.Child elapsedBorrowedHistogramChild;
    private final Histogram.Child elapsedCreationHistogramChild;

    private static Histogram registerHistogram(String name, String help, double bucketStart) {
        return Histogram.build().name(name).labelNames(new String[]{BaseObjectPoolConfig.DEFAULT_JMX_NAME_PREFIX}).help(help).exponentialBuckets(bucketStart, 2.0d, 11).create();
    }

    PrometheusHistogramMetricsTracker(String poolName, CollectorRegistry collectorRegistry) {
        registerMetrics(collectorRegistry);
        this.connectionTimeoutCounterChild = (Counter.Child) CONNECTION_TIMEOUT_COUNTER.labels(new String[]{poolName});
        this.elapsedAcquiredHistogramChild = (Histogram.Child) ELAPSED_ACQUIRED_HISTOGRAM.labels(new String[]{poolName});
        this.elapsedBorrowedHistogramChild = (Histogram.Child) ELAPSED_BORROWED_HISTOGRAM.labels(new String[]{poolName});
        this.elapsedCreationHistogramChild = (Histogram.Child) ELAPSED_CREATION_HISTOGRAM.labels(new String[]{poolName});
    }

    private void registerMetrics(CollectorRegistry collectorRegistry) {
        CONNECTION_TIMEOUT_COUNTER.register(collectorRegistry);
        ELAPSED_ACQUIRED_HISTOGRAM.register(collectorRegistry);
        ELAPSED_BORROWED_HISTOGRAM.register(collectorRegistry);
        ELAPSED_CREATION_HISTOGRAM.register(collectorRegistry);
    }

    @Override // com.zaxxer.hikari.metrics.IMetricsTracker
    public void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {
        this.elapsedAcquiredHistogramChild.observe(elapsedAcquiredNanos);
    }

    @Override // com.zaxxer.hikari.metrics.IMetricsTracker
    public void recordConnectionUsageMillis(long elapsedBorrowedMillis) {
        this.elapsedBorrowedHistogramChild.observe(elapsedBorrowedMillis);
    }

    @Override // com.zaxxer.hikari.metrics.IMetricsTracker
    public void recordConnectionCreatedMillis(long connectionCreatedMillis) {
        this.elapsedCreationHistogramChild.observe(connectionCreatedMillis);
    }

    @Override // com.zaxxer.hikari.metrics.IMetricsTracker
    public void recordConnectionTimeout() {
        this.connectionTimeoutCounterChild.inc();
    }
}
