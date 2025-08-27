package com.zaxxer.hikari.pool;

import java.util.concurrent.ScheduledExecutorService;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/ProxyLeakTaskFactory.class */
class ProxyLeakTaskFactory {
    private ScheduledExecutorService executorService;
    private long leakDetectionThreshold;

    ProxyLeakTaskFactory(long leakDetectionThreshold, ScheduledExecutorService executorService) {
        this.executorService = executorService;
        this.leakDetectionThreshold = leakDetectionThreshold;
    }

    ProxyLeakTask schedule(PoolEntry poolEntry) {
        return this.leakDetectionThreshold == 0 ? ProxyLeakTask.NO_LEAK : scheduleNewTask(poolEntry);
    }

    void updateLeakDetectionThreshold(long leakDetectionThreshold) {
        this.leakDetectionThreshold = leakDetectionThreshold;
    }

    private ProxyLeakTask scheduleNewTask(PoolEntry poolEntry) {
        ProxyLeakTask task = new ProxyLeakTask(poolEntry);
        task.schedule(this.executorService, this.leakDetectionThreshold);
        return task;
    }
}
