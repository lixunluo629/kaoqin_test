package org.apache.commons.pool2.impl;

import java.io.PrintWriter;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/AbandonedConfig.class */
public class AbandonedConfig {
    private boolean removeAbandonedOnBorrow = false;
    private boolean removeAbandonedOnMaintenance = false;
    private int removeAbandonedTimeout = 300;
    private boolean logAbandoned = false;
    private PrintWriter logWriter = new PrintWriter(System.out);
    private boolean useUsageTracking = false;

    public boolean getRemoveAbandonedOnBorrow() {
        return this.removeAbandonedOnBorrow;
    }

    public void setRemoveAbandonedOnBorrow(boolean removeAbandonedOnBorrow) {
        this.removeAbandonedOnBorrow = removeAbandonedOnBorrow;
    }

    public boolean getRemoveAbandonedOnMaintenance() {
        return this.removeAbandonedOnMaintenance;
    }

    public void setRemoveAbandonedOnMaintenance(boolean removeAbandonedOnMaintenance) {
        this.removeAbandonedOnMaintenance = removeAbandonedOnMaintenance;
    }

    public int getRemoveAbandonedTimeout() {
        return this.removeAbandonedTimeout;
    }

    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    public boolean getLogAbandoned() {
        return this.logAbandoned;
    }

    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    public PrintWriter getLogWriter() {
        return this.logWriter;
    }

    public void setLogWriter(PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    public boolean getUseUsageTracking() {
        return this.useUsageTracking;
    }

    public void setUseUsageTracking(boolean useUsageTracking) {
        this.useUsageTracking = useUsageTracking;
    }

    public String toString() {
        return "AbandonedConfig [removeAbandonedOnBorrow=" + this.removeAbandonedOnBorrow + ", removeAbandonedOnMaintenance=" + this.removeAbandonedOnMaintenance + ", removeAbandonedTimeout=" + this.removeAbandonedTimeout + ", logAbandoned=" + this.logAbandoned + ", logWriter=" + this.logWriter + ", useUsageTracking=" + this.useUsageTracking + "]";
    }
}
