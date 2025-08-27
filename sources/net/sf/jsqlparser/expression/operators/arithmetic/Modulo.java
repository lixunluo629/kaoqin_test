package net.sf.jsqlparser.expression.operators.arithmetic;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/arithmetic/Modulo.class */
public class Modulo extends BinaryExpression {
    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override // net.sf.jsqlparser.expression.BinaryExpression
    public String getStringExpression() {
        return QuickTargetSourceCreator.PREFIX_THREAD_LOCAL;
    }
}
