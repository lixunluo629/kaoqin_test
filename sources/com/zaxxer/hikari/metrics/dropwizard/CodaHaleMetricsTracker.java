package com.zaxxer.hikari.metrics.dropwizard;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.PoolStats;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/metrics/dropwizard/CodaHaleMetricsTracker.class */
public final class CodaHaleMetricsTracker implements IMetricsTracker {
    private final String poolName;
    private final Timer connectionObtainTimer;
    private final Histogram connectionUsage;
    private final Histogram connectionCreation;
    private final Meter connectionTimeoutMeter;
    private final MetricRegistry registry;
    private static final String METRIC_CATEGORY = "pool";
    private static final String METRIC_NAME_WAIT = "Wait";
    private static final String METRIC_NAME_USAGE = "Usage";
    private static final String METRIC_NAME_CONNECT = "ConnectionCreation";
    private static final String METRIC_NAME_TIMEOUT_RATE = "ConnectionTimeoutRate";
    private static final String METRIC_NAME_TOTAL_CONNECTIONS = "TotalConnections";
    private static final String METRIC_NAME_IDLE_CONNECTIONS = "IdleConnections";
    private static final String METRIC_NAME_ACTIVE_CONNECTIONS = "ActiveConnections";
    private static final String METRIC_NAME_PENDING_CONNECTIONS = "PendingConnections";
    private static final String METRIC_NAME_MAX_CONNECTIONS = "MaxConnections";
    private static final String METRIC_NAME_MIN_CONNECTIONS = "MinConnections";

    CodaHaleMetricsTracker(String poolName, PoolStats poolStats, MetricRegistry registry) {
        this.poolName = poolName;
        this.registry = registry;
        this.connectionObtainTimer = registry.timer(MetricRegistry.name(poolName, new String[]{"pool", METRIC_NAME_WAIT}));
        this.connectionUsage = registry.histogram(MetricRegistry.name(poolName, new String[]{"pool", METRIC_NAME_USAGE}));
        this.connectionCreation = registry.histogram(MetricRegistry.name(poolName, new String[]{"pool", METRIC_NAME_CONNECT}));
        this.connectionTimeoutMeter = registry.meter(MetricRegistry.name(poolName, new String[]{"pool", METRIC_NAME_TIMEOUT_RATE}));
        String strName = MetricRegistry.name(poolName, new String[]{"pool", METRIC_NAME_TOTAL_CONNECTIONS});
        Objects.requireNonNull(poolStats);
        registry.register(strName, poolStats::getTotalConnections);
        String strName2 = MetricRegistry.name(poolName, new String[]{"pool", METRIC_NAME_IDLE_CONNECTIONS});
        Objects.requireNonNull(poolStats);
        registry.register(strName2, poolStats::getIdleConnections);
        String strName3 = MetricRegistry.name(poolName, new String[]{"pool", METRIC_NAME_ACTIVE_CONNECTIONS});
        Objects.requireNonNull(poolStats);
        registry.register(strName3, poolStats::getActiveConnections);
        String strName4 = MetricRegistry.name(poolName, new String[]{"pool", METRIC_NAME_PENDING_CONNECTIONS});
        Objects.requireNonNull(poolStats);
        registry.register(strName4, poolStats::getPendingThreads);
        String strName5 = MetricRegistry.name(poolName, new String[]{"pool", METRIC_NAME_MAX_CONNECTIONS});
        Objects.requireNonNull(poolStats);
        registry.register(strName5, poolStats::getMaxConnections);
        String strName6 = MetricRegistry.name(poolName, new String[]{"pool", METRIC_NAME_MIN_CONNECTIONS});
        Objects.requireNonNull(poolStats);
        registry.register(strName6, poolStats::getMinConnections);
    }

    @Override // com.zaxxer.hikari.metrics.IMetricsTracker, java.lang.AutoCloseable
    public void close() {
        this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", METRIC_NAME_WAIT}));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", METRIC_NAME_USAGE}));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", METRIC_NAME_CONNECT}));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", METRIC_NAME_TIMEOUT_RATE}));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", METRIC_NAME_TOTAL_CONNECTIONS}));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", METRIC_NAME_IDLE_CONNECTIONS}));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", METRIC_NAME_ACTIVE_CONNECTIONS}));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", METRIC_NAME_PENDING_CONNECTIONS}));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", METRIC_NAME_MAX_CONNECTIONS}));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", METRIC_NAME_MIN_CONNECTIONS}));
    }

    @Override // com.zaxxer.hikari.metrics.IMetricsTracker
    public void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {
        this.connectionObtainTimer.update(elapsedAcquiredNanos, TimeUnit.NANOSECONDS);
    }

    @Override // com.zaxxer.hikari.metrics.IMetricsTracker
    public void recordConnectionUsageMillis(long elapsedBorrowedMillis) {
        this.connectionUsage.update(elapsedBorrowedMillis);
    }

    @Override // com.zaxxer.hikari.metrics.IMetricsTracker
    public void recordConnectionTimeout() {
        this.connectionTimeoutMeter.mark();
    }

    @Override // com.zaxxer.hikari.metrics.IMetricsTracker
    public void recordConnectionCreatedMillis(long connectionCreatedMillis) {
        this.connectionCreation.update(connectionCreatedMillis);
    }

    public Timer getConnectionAcquisitionTimer() {
        return this.connectionObtainTimer;
    }

    public Histogram getConnectionDurationHistogram() {
        return this.connectionUsage;
    }

    public Histogram getConnectionCreationHistogram() {
        return this.connectionCreation;
    }
}
