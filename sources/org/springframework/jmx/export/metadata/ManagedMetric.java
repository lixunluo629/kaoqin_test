package org.springframework.jmx.export.metadata;

import org.springframework.jmx.support.MetricType;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/metadata/ManagedMetric.class */
public class ManagedMetric extends AbstractJmxAttribute {
    private String category = "";
    private String displayName = "";
    private MetricType metricType = MetricType.GAUGE;
    private int persistPeriod = -1;
    private String persistPolicy = "";
    private String unit = "";

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setMetricType(MetricType metricType) {
        Assert.notNull(metricType, "MetricType must not be null");
        this.metricType = metricType;
    }

    public MetricType getMetricType() {
        return this.metricType;
    }

    public void setPersistPeriod(int persistPeriod) {
        this.persistPeriod = persistPeriod;
    }

    public int getPersistPeriod() {
        return this.persistPeriod;
    }

    public void setPersistPolicy(String persistPolicy) {
        this.persistPolicy = persistPolicy;
    }

    public String getPersistPolicy() {
        return this.persistPolicy;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return this.unit;
    }
}
