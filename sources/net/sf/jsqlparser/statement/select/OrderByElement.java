package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.expression.Expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/OrderByElement.class */
public class OrderByElement {
    private Expression expression;
    private NullOrdering nullOrdering;
    private boolean asc = true;
    private boolean ascDesc = false;

    /* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/OrderByElement$NullOrdering.class */
    public enum NullOrdering {
        NULLS_FIRST,
        NULLS_LAST
    }

    public boolean isAsc() {
        return this.asc;
    }

    public NullOrdering getNullOrdering() {
        return this.nullOrdering;
    }

    public void setNullOrdering(NullOrdering nullOrdering) {
        this.nullOrdering = nullOrdering;
    }

    public void setAsc(boolean b) {
        this.asc = b;
    }

    public void setAscDescPresent(boolean b) {
        this.ascDesc = b;
    }

    public boolean isAscDescPresent() {
        return this.ascDesc;
    }

    public void accept(OrderByVisitor orderByVisitor) {
        orderByVisitor.visit(this);
    }

    public Expression getExpression() {
        return this.expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(this.expression.toString());
        if (!this.asc) {
            b.append(" DESC");
        } else if (this.ascDesc) {
            b.append(" ASC");
        }
        if (this.nullOrdering != null) {
            b.append(' ');
            b.append(this.nullOrdering == NullOrdering.NULLS_FIRST ? "NULLS FIRST" : "NULLS LAST");
        }
        return b.toString();
    }
}
