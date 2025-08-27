package net.sf.jsqlparser.expression.operators.relational;

import ch.qos.logback.core.joran.action.ActionConst;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/IsNullExpression.class */
public class IsNullExpression implements Expression {
    private Expression leftExpression;
    private boolean not = false;

    public Expression getLeftExpression() {
        return this.leftExpression;
    }

    public boolean isNot() {
        return this.not;
    }

    public void setLeftExpression(Expression expression) {
        this.leftExpression = expression;
    }

    public void setNot(boolean b) {
        this.not = b;
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String toString() {
        return this.leftExpression + " IS " + (this.not ? "NOT " : "") + ActionConst.NULL;
    }
}
