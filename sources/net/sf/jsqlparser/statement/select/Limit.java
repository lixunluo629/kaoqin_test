package net.sf.jsqlparser.statement.select;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/Limit.class */
public class Limit {
    private long offset;
    private long rowCount;
    private boolean limitAll;
    private boolean rowCountJdbcParameter = false;
    private boolean offsetJdbcParameter = false;
    private boolean limitNull = false;

    public long getOffset() {
        return this.offset;
    }

    public long getRowCount() {
        return this.rowCount;
    }

    public void setOffset(long l) {
        this.offset = l;
    }

    public void setRowCount(long l) {
        this.rowCount = l;
    }

    public boolean isOffsetJdbcParameter() {
        return this.offsetJdbcParameter;
    }

    public boolean isRowCountJdbcParameter() {
        return this.rowCountJdbcParameter;
    }

    public void setOffsetJdbcParameter(boolean b) {
        this.offsetJdbcParameter = b;
    }

    public void setRowCountJdbcParameter(boolean b) {
        this.rowCountJdbcParameter = b;
    }

    public boolean isLimitAll() {
        return this.limitAll;
    }

    public void setLimitAll(boolean b) {
        this.limitAll = b;
    }

    public boolean isLimitNull() {
        return this.limitNull;
    }

    public void setLimitNull(boolean b) {
        this.limitNull = b;
    }

    public String toString() {
        String retVal = "";
        if (this.limitNull) {
            retVal = retVal + " LIMIT NULL";
        } else if (this.rowCount >= 0 || this.rowCountJdbcParameter) {
            retVal = retVal + " LIMIT " + (this.rowCountJdbcParameter ? "?" : this.rowCount + "");
        }
        if (this.offset > 0 || this.offsetJdbcParameter) {
            retVal = retVal + " OFFSET " + (this.offsetJdbcParameter ? "?" : this.offset + "");
        }
        return retVal;
    }
}
