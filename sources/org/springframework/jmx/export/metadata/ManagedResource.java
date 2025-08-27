package org.springframework.jmx.export.metadata;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/metadata/ManagedResource.class */
public class ManagedResource extends AbstractJmxAttribute {
    private String objectName;
    private String logFile;
    private String persistPolicy;
    private String persistName;
    private String persistLocation;
    private boolean log = false;
    private int persistPeriod = -1;

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public boolean isLog() {
        return this.log;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getLogFile() {
        return this.logFile;
    }

    public void setPersistPolicy(String persistPolicy) {
        this.persistPolicy = persistPolicy;
    }

    public String getPersistPolicy() {
        return this.persistPolicy;
    }

    public void setPersistPeriod(int persistPeriod) {
        this.persistPeriod = persistPeriod;
    }

    public int getPersistPeriod() {
        return this.persistPeriod;
    }

    public void setPersistName(String persistName) {
        this.persistName = persistName;
    }

    public String getPersistName() {
        return this.persistName;
    }

    public void setPersistLocation(String persistLocation) {
        this.persistLocation = persistLocation;
    }

    public String getPersistLocation() {
        return this.persistLocation;
    }
}
