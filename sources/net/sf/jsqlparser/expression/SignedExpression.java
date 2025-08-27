package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/SignedExpression.class */
public class SignedExpression implements Expression {
    private char sign;
    private Expression expression;

    public SignedExpression(char sign, Expression expression) {
        setSign(sign);
        setExpression(expression);
    }

    public char getSign() {
        return this.sign;
    }

    public final void setSign(char sign) {
        this.sign = sign;
        if (sign != '+' && sign != '-') {
            throw new IllegalArgumentException("illegal sign character, only + - allowed");
        }
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

    public String toString() {
        return getSign() + this.expression.toString();
    }
}
