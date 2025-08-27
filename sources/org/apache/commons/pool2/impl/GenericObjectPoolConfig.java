package org.apache.commons.pool2.impl;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/GenericObjectPoolConfig.class */
public class GenericObjectPoolConfig extends BaseObjectPoolConfig {
    public static final int DEFAULT_MAX_TOTAL = 8;
    public static final int DEFAULT_MAX_IDLE = 8;
    public static final int DEFAULT_MIN_IDLE = 0;
    private int maxTotal = 8;
    private int maxIdle = 8;
    private int minIdle = 0;

    public int getMaxTotal() {
        return this.maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return this.maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return this.minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public GenericObjectPoolConfig m3070clone() {
        try {
            return (GenericObjectPoolConfig) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override // org.apache.commons.pool2.impl.BaseObjectPoolConfig, org.apache.commons.pool2.BaseObject
    protected void toStringAppendFields(StringBuilder builder) {
        super.toStringAppendFields(builder);
        builder.append(", maxTotal=");
        builder.append(this.maxTotal);
        builder.append(", maxIdle=");
        builder.append(this.maxIdle);
        builder.append(", minIdle=");
        builder.append(this.minIdle);
    }
}
