package net.sf.jsqlparser.expression;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/BinaryExpression.class */
public abstract class BinaryExpression implements Expression {
    private Expression leftExpression;
    private Expression rightExpression;
    private boolean not = false;

    public abstract String getStringExpression();

    public Expression getLeftExpression() {
        return this.leftExpression;
    }

    public Expression getRightExpression() {
        return this.rightExpression;
    }

    public void setLeftExpression(Expression expression) {
        this.leftExpression = expression;
    }

    public void setRightExpression(Expression expression) {
        this.rightExpression = expression;
    }

    public void setNot() {
        this.not = true;
    }

    public boolean isNot() {
        return this.not;
    }

    public String toString() {
        return (this.not ? "NOT " : "") + getLeftExpression() + SymbolConstants.SPACE_SYMBOL + getStringExpression() + SymbolConstants.SPACE_SYMBOL + getRightExpression();
    }
}
