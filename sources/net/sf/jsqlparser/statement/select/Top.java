package net.sf.jsqlparser.statement.select;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/Top.class */
public class Top {
    private long rowCount;
    private boolean rowCountJdbcParameter = false;
    private boolean hasParenthesis = false;
    private boolean isPercentage = false;

    public long getRowCount() {
        return this.rowCount;
    }

    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }

    public boolean isRowCountJdbcParameter() {
        return this.rowCountJdbcParameter;
    }

    public void setRowCountJdbcParameter(boolean rowCountJdbcParameter) {
        this.rowCountJdbcParameter = rowCountJdbcParameter;
    }

    public boolean hasParenthesis() {
        return this.hasParenthesis;
    }

    public void setParenthesis(boolean hasParenthesis) {
        this.hasParenthesis = hasParenthesis;
    }

    public boolean isPercentage() {
        return this.isPercentage;
    }

    public void setPercentage(boolean percentage) {
        this.isPercentage = percentage;
    }

    public String toString() {
        String result = "TOP ";
        if (this.hasParenthesis) {
            result = result + "(";
        }
        String result2 = result + (this.rowCountJdbcParameter ? "?" : Long.valueOf(this.rowCount));
        if (this.hasParenthesis) {
            result2 = result2 + ")";
        }
        if (this.isPercentage) {
            result2 = result2 + " PERCENT";
        }
        return result2;
    }
}
