package net.sf.jsqlparser.expression.operators.relational;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/ExistsExpression.class */
public class ExistsExpression implements Expression {
    private Expression rightExpression;
    private boolean not = false;

    public Expression getRightExpression() {
        return this.rightExpression;
    }

    public void setRightExpression(Expression expression) {
        this.rightExpression = expression;
    }

    public boolean isNot() {
        return this.not;
    }

    public void setNot(boolean b) {
        this.not = b;
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String getStringExpression() {
        return (this.not ? "NOT " : "") + "EXISTS";
    }

    public String toString() {
        return getStringExpression() + SymbolConstants.SPACE_SYMBOL + this.rightExpression.toString();
    }
}
