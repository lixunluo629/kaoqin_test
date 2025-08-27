package org.apache.ibatis.session;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/TransactionIsolationLevel.class */
public enum TransactionIsolationLevel {
    NONE(0),
    READ_COMMITTED(2),
    READ_UNCOMMITTED(1),
    REPEATABLE_READ(4),
    SERIALIZABLE(8);

    private final int level;

    TransactionIsolationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
}
