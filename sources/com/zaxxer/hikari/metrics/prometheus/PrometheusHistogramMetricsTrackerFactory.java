package com.zaxxer.hikari.metrics.prometheus;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;
import io.prometheus.client.CollectorRegistry;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/metrics/prometheus/PrometheusHistogramMetricsTrackerFactory.class */
public class PrometheusHistogramMetricsTrackerFactory implements MetricsTrackerFactory {
    private HikariCPCollector collector;
    private CollectorRegistry collectorRegistry;

    public PrometheusHistogramMetricsTrackerFactory() {
        this.collectorRegistry = CollectorRegistry.defaultRegistry;
    }

    public PrometheusHistogramMetricsTrackerFactory(CollectorRegistry collectorRegistry) {
        this.collectorRegistry = collectorRegistry;
    }

    @Override // com.zaxxer.hikari.metrics.MetricsTrackerFactory
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        getCollector().add(poolName, poolStats);
        return new PrometheusHistogramMetricsTracker(poolName, this.collectorRegistry);
    }

    private HikariCPCollector getCollector() {
        if (this.collector == null) {
            this.collector = (HikariCPCollector) new HikariCPCollector().register(this.collectorRegistry);
        }
        return this.collector;
    }
}
