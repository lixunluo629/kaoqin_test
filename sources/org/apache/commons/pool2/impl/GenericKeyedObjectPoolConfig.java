package org.apache.commons.pool2.impl;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/GenericKeyedObjectPoolConfig.class */
public class GenericKeyedObjectPoolConfig extends BaseObjectPoolConfig {
    public static final int DEFAULT_MAX_TOTAL_PER_KEY = 8;
    public static final int DEFAULT_MAX_TOTAL = -1;
    public static final int DEFAULT_MIN_IDLE_PER_KEY = 0;
    public static final int DEFAULT_MAX_IDLE_PER_KEY = 8;
    private int minIdlePerKey = 0;
    private int maxIdlePerKey = 8;
    private int maxTotalPerKey = 8;
    private int maxTotal = -1;

    public int getMaxTotal() {
        return this.maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxTotalPerKey() {
        return this.maxTotalPerKey;
    }

    public void setMaxTotalPerKey(int maxTotalPerKey) {
        this.maxTotalPerKey = maxTotalPerKey;
    }

    public int getMinIdlePerKey() {
        return this.minIdlePerKey;
    }

    public void setMinIdlePerKey(int minIdlePerKey) {
        this.minIdlePerKey = minIdlePerKey;
    }

    public int getMaxIdlePerKey() {
        return this.maxIdlePerKey;
    }

    public void setMaxIdlePerKey(int maxIdlePerKey) {
        this.maxIdlePerKey = maxIdlePerKey;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public GenericKeyedObjectPoolConfig m3069clone() {
        try {
            return (GenericKeyedObjectPoolConfig) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override // org.apache.commons.pool2.impl.BaseObjectPoolConfig, org.apache.commons.pool2.BaseObject
    protected void toStringAppendFields(StringBuilder builder) {
        super.toStringAppendFields(builder);
        builder.append(", minIdlePerKey=");
        builder.append(this.minIdlePerKey);
        builder.append(", maxIdlePerKey=");
        builder.append(this.maxIdlePerKey);
        builder.append(", maxTotalPerKey=");
        builder.append(this.maxTotalPerKey);
        builder.append(", maxTotal=");
        builder.append(this.maxTotal);
    }
}
