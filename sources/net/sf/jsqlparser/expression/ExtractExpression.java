package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/ExtractExpression.class */
public class ExtractExpression implements Expression {
    private String name;
    private Expression expression;

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public String toString() {
        return "EXTRACT(" + this.name + " FROM " + this.expression + ')';
    }
}
