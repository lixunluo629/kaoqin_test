package net.sf.jsqlparser.expression.operators.relational;

import net.sf.jsqlparser.expression.ExpressionVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/GreaterThanEquals.class */
public class GreaterThanEquals extends OldOracleJoinBinaryExpression {
    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override // net.sf.jsqlparser.expression.BinaryExpression
    public String getStringExpression() {
        return ">=";
    }
}
