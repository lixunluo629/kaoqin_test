package org.springframework.jmx.export.metadata;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/metadata/ManagedAttribute.class */
public class ManagedAttribute extends AbstractJmxAttribute {
    public static final ManagedAttribute EMPTY = new ManagedAttribute();
    private Object defaultValue;
    private String persistPolicy;
    private int persistPeriod = -1;

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
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
}
