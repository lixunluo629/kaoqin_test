package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/JdbcParameter.class */
public class JdbcParameter implements Expression {
    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String toString() {
        return "?";
    }
}
