package com.zaxxer.hikari.metrics.prometheus;

import com.zaxxer.hikari.metrics.PoolStats;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/metrics/prometheus/HikariCPCollector.class */
class HikariCPCollector extends Collector {
    private static final List<String> LABEL_NAMES = Collections.singletonList(BaseObjectPoolConfig.DEFAULT_JMX_NAME_PREFIX);
    private final Map<String, PoolStats> poolStatsMap = new ConcurrentHashMap();

    HikariCPCollector() {
    }

    public List<Collector.MetricFamilySamples> collect() {
        return Arrays.asList(createGauge("hikaricp_active_connections", "Active connections", (v0) -> {
            return v0.getActiveConnections();
        }), createGauge("hikaricp_idle_connections", "Idle connections", (v0) -> {
            return v0.getIdleConnections();
        }), createGauge("hikaricp_pending_threads", "Pending threads", (v0) -> {
            return v0.getPendingThreads();
        }), createGauge("hikaricp_connections", "The number of current connections", (v0) -> {
            return v0.getTotalConnections();
        }), createGauge("hikaricp_max_connections", "Max connections", (v0) -> {
            return v0.getMaxConnections();
        }), createGauge("hikaricp_min_connections", "Min connections", (v0) -> {
            return v0.getMinConnections();
        }));
    }

    void add(String name, PoolStats poolStats) {
        this.poolStatsMap.put(name, poolStats);
    }

    void remove(String name) {
        this.poolStatsMap.remove(name);
    }

    private GaugeMetricFamily createGauge(String metric, String help, Function<PoolStats, Integer> metricValueFunction) {
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(metric, help, LABEL_NAMES);
        this.poolStatsMap.forEach((k, v) -> {
            metricFamily.addMetric(Collections.singletonList(k), ((Integer) metricValueFunction.apply(v)).intValue());
        });
        return metricFamily;
    }
}
