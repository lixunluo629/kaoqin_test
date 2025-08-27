package org.apache.commons.pool2.impl;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/EvictionConfig.class */
public class EvictionConfig {
    private final long idleEvictTime;
    private final long idleSoftEvictTime;
    private final int minIdle;

    public EvictionConfig(long poolIdleEvictTime, long poolIdleSoftEvictTime, int minIdle) {
        if (poolIdleEvictTime > 0) {
            this.idleEvictTime = poolIdleEvictTime;
        } else {
            this.idleEvictTime = Long.MAX_VALUE;
        }
        if (poolIdleSoftEvictTime > 0) {
            this.idleSoftEvictTime = poolIdleSoftEvictTime;
        } else {
            this.idleSoftEvictTime = Long.MAX_VALUE;
        }
        this.minIdle = minIdle;
    }

    public long getIdleEvictTime() {
        return this.idleEvictTime;
    }

    public long getIdleSoftEvictTime() {
        return this.idleSoftEvictTime;
    }

    public int getMinIdle() {
        return this.minIdle;
    }

    public String toString() {
        return "EvictionConfig [idleEvictTime=" + this.idleEvictTime + ", idleSoftEvictTime=" + this.idleSoftEvictTime + ", minIdle=" + this.minIdle + "]";
    }
}
