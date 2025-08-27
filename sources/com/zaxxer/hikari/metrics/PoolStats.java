package com.zaxxer.hikari.metrics;

import com.zaxxer.hikari.util.ClockSource;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/metrics/PoolStats.class */
public abstract class PoolStats {
    private final AtomicLong reloadAt = new AtomicLong();
    private final long timeoutMs;
    protected volatile int totalConnections;
    protected volatile int idleConnections;
    protected volatile int activeConnections;
    protected volatile int pendingThreads;
    protected volatile int maxConnections;
    protected volatile int minConnections;

    protected abstract void update();

    public PoolStats(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public int getTotalConnections() {
        if (shouldLoad()) {
            update();
        }
        return this.totalConnections;
    }

    public int getIdleConnections() {
        if (shouldLoad()) {
            update();
        }
        return this.idleConnections;
    }

    public int getActiveConnections() {
        if (shouldLoad()) {
            update();
        }
        return this.activeConnections;
    }

    public int getPendingThreads() {
        if (shouldLoad()) {
            update();
        }
        return this.pendingThreads;
    }

    public int getMaxConnections() {
        if (shouldLoad()) {
            update();
        }
        return this.maxConnections;
    }

    public int getMinConnections() {
        if (shouldLoad()) {
            update();
        }
        return this.minConnections;
    }

    private boolean shouldLoad() {
        long now;
        long reloadTime;
        do {
            now = ClockSource.currentTime();
            reloadTime = this.reloadAt.get();
            if (reloadTime > now) {
                return false;
            }
        } while (!this.reloadAt.compareAndSet(reloadTime, ClockSource.plusMillis(now, this.timeoutMs)));
        return true;
    }
}
