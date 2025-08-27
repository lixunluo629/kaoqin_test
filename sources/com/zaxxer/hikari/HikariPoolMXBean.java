package com.zaxxer.hikari;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/HikariPoolMXBean.class */
public interface HikariPoolMXBean {
    int getIdleConnections();

    int getActiveConnections();

    int getTotalConnections();

    int getThreadsAwaitingConnection();

    void softEvictConnections();

    void suspendPool();

    void resumePool();
}
