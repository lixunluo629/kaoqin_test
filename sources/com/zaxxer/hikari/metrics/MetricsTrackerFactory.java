package com.zaxxer.hikari.metrics;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/metrics/MetricsTrackerFactory.class */
public interface MetricsTrackerFactory {
    IMetricsTracker create(String str, PoolStats poolStats);
}
