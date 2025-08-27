package org.apache.ibatis.session;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/RowBounds.class */
public class RowBounds {
    public static final int NO_ROW_OFFSET = 0;
    public static final int NO_ROW_LIMIT = Integer.MAX_VALUE;
    public static final RowBounds DEFAULT = new RowBounds();
    private final int offset;
    private final int limit;

    public RowBounds() {
        this.offset = 0;
        this.limit = Integer.MAX_VALUE;
    }

    public RowBounds(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLimit() {
        return this.limit;
    }
}
