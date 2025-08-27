package com.zaxxer.hikari.metrics;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/metrics/IMetricsTracker.class */
public interface IMetricsTracker extends AutoCloseable {
    default void recordConnectionCreatedMillis(long connectionCreatedMillis) {
    }

    default void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {
    }

    default void recordConnectionUsageMillis(long elapsedBorrowedMillis) {
    }

    default void recordConnectionTimeout() {
    }

    @Override // java.lang.AutoCloseable
    default void close() {
    }
}
