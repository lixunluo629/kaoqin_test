package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/SelectExpressionItem.class */
public class SelectExpressionItem implements SelectItem {
    private Expression expression;
    private Alias alias;

    public SelectExpressionItem() {
    }

    public SelectExpressionItem(Expression expression) {
        this.expression = expression;
    }

    public Alias getAlias() {
        return this.alias;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override // net.sf.jsqlparser.statement.select.SelectItem
    public void accept(SelectItemVisitor selectItemVisitor) {
        selectItemVisitor.visit(this);
    }

    public String toString() {
        return this.expression + (this.alias != null ? this.alias.toString() : "");
    }
}
