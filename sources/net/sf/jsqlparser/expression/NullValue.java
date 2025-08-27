package net.sf.jsqlparser.expression;

import ch.qos.logback.core.joran.action.ActionConst;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/NullValue.class */
public class NullValue implements Expression {
    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String toString() {
        return ActionConst.NULL;
    }
}
