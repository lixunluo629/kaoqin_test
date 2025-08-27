package net.sf.jsqlparser.expression.operators.relational;

import net.sf.jsqlparser.expression.ExpressionVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/NotEqualsTo.class */
public class NotEqualsTo extends OldOracleJoinBinaryExpression {
    private final String operator;

    public NotEqualsTo() {
        this.operator = "<>";
    }

    public NotEqualsTo(String operator) {
        this.operator = operator;
        if (!"!=".equals(operator) && !"<>".equals(operator)) {
            throw new IllegalArgumentException("only <> or != allowed");
        }
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override // net.sf.jsqlparser.expression.BinaryExpression
    public String getStringExpression() {
        return this.operator;
    }
}
