package net.sf.jsqlparser.expression;

import net.sf.jsqlparser.statement.select.SubSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/AllComparisonExpression.class */
public class AllComparisonExpression implements Expression {
    private SubSelect subSelect;

    public AllComparisonExpression(SubSelect subSelect) {
        this.subSelect = subSelect;
    }

    public SubSelect getSubSelect() {
        return this.subSelect;
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }
}
