package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/Parenthesis.class */
public class Parenthesis implements Expression {
    private Expression expression;
    private boolean not = false;

    public Parenthesis() {
    }

    public Parenthesis(Expression expression) {
        setExpression(expression);
    }

    public Expression getExpression() {
        return this.expression;
    }

    public final void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public void setNot() {
        this.not = true;
    }

    public boolean isNot() {
        return this.not;
    }

    public String toString() {
        return (this.not ? "NOT " : "") + "(" + this.expression + ")";
    }
}
