package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/WhenClause.class */
public class WhenClause implements Expression {
    private Expression whenExpression;
    private Expression thenExpression;

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Expression getThenExpression() {
        return this.thenExpression;
    }

    public void setThenExpression(Expression thenExpression) {
        this.thenExpression = thenExpression;
    }

    public Expression getWhenExpression() {
        return this.whenExpression;
    }

    public void setWhenExpression(Expression whenExpression) {
        this.whenExpression = whenExpression;
    }

    public String toString() {
        return "WHEN " + this.whenExpression + " THEN " + this.thenExpression;
    }
}
