package com.zaxxer.hikari;

import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.pool.HikariPool;
import java.io.Closeable;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/HikariDataSource.class */
public class HikariDataSource extends HikariConfig implements DataSource, Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) HikariDataSource.class);
    private final AtomicBoolean isShutdown;
    private final HikariPool fastPathPool;
    private volatile HikariPool pool;

    public HikariDataSource() {
        this.isShutdown = new AtomicBoolean();
        this.fastPathPool = null;
    }

    public HikariDataSource(HikariConfig configuration) throws IllegalAccessException, IllegalArgumentException {
        this.isShutdown = new AtomicBoolean();
        configuration.validate();
        configuration.copyStateTo(this);
        LOGGER.info("{} - Starting...", configuration.getPoolName());
        HikariPool hikariPool = new HikariPool(this);
        this.fastPathPool = hikariPool;
        this.pool = hikariPool;
        LOGGER.info("{} - Start completed.", configuration.getPoolName());
        seal();
    }

    @Override // javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        if (isClosed()) {
            throw new SQLException("HikariDataSource " + this + " has been closed.");
        }
        if (this.fastPathPool != null) {
            return this.fastPathPool.getConnection();
        }
        HikariPool result = this.pool;
        if (result == null) {
            synchronized (this) {
                result = this.pool;
                if (result == null) {
                    validate();
                    LOGGER.info("{} - Starting...", getPoolName());
                    try {
                        HikariPool hikariPool = new HikariPool(this);
                        result = hikariPool;
                        this.pool = hikariPool;
                        seal();
                        LOGGER.info("{} - Start completed.", getPoolName());
                    } catch (HikariPool.PoolInitializationException pie) {
                        if (pie.getCause() instanceof SQLException) {
                            throw ((SQLException) pie.getCause());
                        }
                        throw pie;
                    }
                }
            }
        }
        return result.getConnection();
    }

    @Override // javax.sql.DataSource
    public Connection getConnection(String username, String password) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // javax.sql.CommonDataSource
    public PrintWriter getLogWriter() throws SQLException {
        HikariPool p = this.pool;
        if (p != null) {
            return p.getUnwrappedDataSource().getLogWriter();
        }
        return null;
    }

    @Override // javax.sql.CommonDataSource
    public void setLogWriter(PrintWriter out) throws SQLException {
        HikariPool p = this.pool;
        if (p != null) {
            p.getUnwrappedDataSource().setLogWriter(out);
        }
    }

    @Override // javax.sql.CommonDataSource
    public void setLoginTimeout(int seconds) throws SQLException {
        HikariPool p = this.pool;
        if (p != null) {
            p.getUnwrappedDataSource().setLoginTimeout(seconds);
        }
    }

    @Override // javax.sql.CommonDataSource
    public int getLoginTimeout() throws SQLException {
        HikariPool p = this.pool;
        if (p != null) {
            return p.getUnwrappedDataSource().getLoginTimeout();
        }
        return 0;
    }

    @Override // javax.sql.CommonDataSource
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v7, types: [T, java.lang.Object, javax.sql.DataSource] */
    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> cls) throws SQLException {
        if (cls.isInstance(this)) {
            return this;
        }
        HikariPool hikariPool = this.pool;
        if (hikariPool != null) {
            ?? r0 = (T) hikariPool.getUnwrappedDataSource();
            if (cls.isInstance(r0)) {
                return r0;
            }
            if (r0 != 0) {
                return (T) r0.unwrap(cls);
            }
        }
        throw new SQLException("Wrapped DataSource is not an instance of " + cls);
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return true;
        }
        HikariPool p = this.pool;
        if (p != null) {
            DataSource unwrappedDataSource = p.getUnwrappedDataSource();
            if (iface.isInstance(unwrappedDataSource)) {
                return true;
            }
            if (unwrappedDataSource != null) {
                return unwrappedDataSource.isWrapperFor(iface);
            }
            return false;
        }
        return false;
    }

    @Override // com.zaxxer.hikari.HikariConfig
    public void setMetricRegistry(Object metricRegistry) {
        boolean isAlreadySet = getMetricRegistry() != null;
        super.setMetricRegistry(metricRegistry);
        HikariPool p = this.pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricRegistry can only be set one time");
            }
            p.setMetricRegistry(super.getMetricRegistry());
        }
    }

    @Override // com.zaxxer.hikari.HikariConfig
    public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
        boolean isAlreadySet = getMetricsTrackerFactory() != null;
        super.setMetricsTrackerFactory(metricsTrackerFactory);
        HikariPool p = this.pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricsTrackerFactory can only be set one time");
            }
            p.setMetricsTrackerFactory(super.getMetricsTrackerFactory());
        }
    }

    @Override // com.zaxxer.hikari.HikariConfig
    public void setHealthCheckRegistry(Object healthCheckRegistry) {
        boolean isAlreadySet = getHealthCheckRegistry() != null;
        super.setHealthCheckRegistry(healthCheckRegistry);
        HikariPool p = this.pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("HealthCheckRegistry can only be set one time");
            }
            p.setHealthCheckRegistry(super.getHealthCheckRegistry());
        }
    }

    public boolean isRunning() {
        return this.pool != null && this.pool.poolState == 0;
    }

    public HikariPoolMXBean getHikariPoolMXBean() {
        return this.pool;
    }

    public HikariConfigMXBean getHikariConfigMXBean() {
        return this;
    }

    public void evictConnection(Connection connection) {
        HikariPool p;
        if (!isClosed() && (p = this.pool) != null && connection.getClass().getName().startsWith("com.zaxxer.hikari")) {
            p.evictConnection(connection);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        HikariPool p;
        if (!this.isShutdown.getAndSet(true) && (p = this.pool) != null) {
            try {
                LOGGER.info("{} - Shutdown initiated...", getPoolName());
                p.shutdown();
                LOGGER.info("{} - Shutdown completed.", getPoolName());
            } catch (InterruptedException e) {
                LOGGER.warn("{} - Interrupted during closing", getPoolName(), e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isClosed() {
        return this.isShutdown.get();
    }

    public String toString() {
        return "HikariDataSource (" + this.pool + ")";
    }
}
