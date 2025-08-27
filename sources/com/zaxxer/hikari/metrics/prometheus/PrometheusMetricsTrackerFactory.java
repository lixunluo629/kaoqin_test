package com.zaxxer.hikari.metrics.prometheus;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/metrics/prometheus/PrometheusMetricsTrackerFactory.class */
public class PrometheusMetricsTrackerFactory implements MetricsTrackerFactory {
    private static final Map<CollectorRegistry, RegistrationStatus> registrationStatuses = new ConcurrentHashMap();
    private final HikariCPCollector collector;
    private final CollectorRegistry collectorRegistry;

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/metrics/prometheus/PrometheusMetricsTrackerFactory$RegistrationStatus.class */
    public enum RegistrationStatus {
        REGISTERED
    }

    public PrometheusMetricsTrackerFactory() {
        this(CollectorRegistry.defaultRegistry);
    }

    public PrometheusMetricsTrackerFactory(CollectorRegistry collectorRegistry) {
        this.collector = new HikariCPCollector();
        this.collectorRegistry = collectorRegistry;
    }

    @Override // com.zaxxer.hikari.metrics.MetricsTrackerFactory
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        registerCollector(this.collector, this.collectorRegistry);
        this.collector.add(poolName, poolStats);
        return new PrometheusMetricsTracker(poolName, this.collectorRegistry, this.collector);
    }

    private void registerCollector(Collector collector, CollectorRegistry collectorRegistry) {
        if (registrationStatuses.putIfAbsent(collectorRegistry, RegistrationStatus.REGISTERED) == null) {
            collector.register(collectorRegistry);
        }
    }
}
