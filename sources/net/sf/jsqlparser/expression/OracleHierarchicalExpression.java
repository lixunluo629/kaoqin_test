package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/OracleHierarchicalExpression.class */
public class OracleHierarchicalExpression implements Expression {
    private Expression startExpression;
    private Expression connectExpression;
    private boolean noCycle = false;

    public Expression getStartExpression() {
        return this.startExpression;
    }

    public void setStartExpression(Expression startExpression) {
        this.startExpression = startExpression;
    }

    public Expression getConnectExpression() {
        return this.connectExpression;
    }

    public void setConnectExpression(Expression connectExpression) {
        this.connectExpression = connectExpression;
    }

    public boolean isNoCycle() {
        return this.noCycle;
    }

    public void setNoCycle(boolean noCycle) {
        this.noCycle = noCycle;
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        if (this.startExpression != null) {
            b.append(" START WITH ").append(this.startExpression.toString());
        }
        b.append(" CONNECT BY ");
        if (isNoCycle()) {
            b.append("NOCYCLE ");
        }
        b.append(this.connectExpression.toString());
        return b.toString();
    }
}
