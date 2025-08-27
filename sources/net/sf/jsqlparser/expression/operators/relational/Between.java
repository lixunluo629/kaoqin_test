package net.sf.jsqlparser.expression.operators.relational;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/Between.class */
public class Between implements Expression {
    private Expression leftExpression;
    private boolean not = false;
    private Expression betweenExpressionStart;
    private Expression betweenExpressionEnd;

    public Expression getBetweenExpressionEnd() {
        return this.betweenExpressionEnd;
    }

    public Expression getBetweenExpressionStart() {
        return this.betweenExpressionStart;
    }

    public Expression getLeftExpression() {
        return this.leftExpression;
    }

    public boolean isNot() {
        return this.not;
    }

    public void setBetweenExpressionEnd(Expression expression) {
        this.betweenExpressionEnd = expression;
    }

    public void setBetweenExpressionStart(Expression expression) {
        this.betweenExpressionStart = expression;
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
        return this.leftExpression + SymbolConstants.SPACE_SYMBOL + (this.not ? "NOT " : "") + "BETWEEN " + this.betweenExpressionStart + " AND " + this.betweenExpressionEnd;
    }
}
