package net.sf.jsqlparser.expression.operators.conditional;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/conditional/AndExpression.class */
public class AndExpression extends BinaryExpression {
    public AndExpression(Expression leftExpression, Expression rightExpression) {
        setLeftExpression(leftExpression);
        setRightExpression(rightExpression);
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override // net.sf.jsqlparser.expression.BinaryExpression
    public String getStringExpression() {
        return "AND";
    }
}
