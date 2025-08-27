package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/ExpressionListItem.class */
public class ExpressionListItem {
    private ExpressionList expressionList;
    private Alias alias;

    public ExpressionList getExpressionList() {
        return this.expressionList;
    }

    public void setExpressionList(ExpressionList expressionList) {
        this.expressionList = expressionList;
    }

    public Alias getAlias() {
        return this.alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public String toString() {
        return this.expressionList + (this.alias != null ? this.alias.toString() : "");
    }
}
