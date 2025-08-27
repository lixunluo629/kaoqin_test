package net.sf.jsqlparser.expression.operators.relational;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/InExpression.class */
public class InExpression implements Expression, SupportsOldOracleJoinSyntax {
    private Expression leftExpression;
    private ItemsList leftItemsList;
    private ItemsList rightItemsList;
    private boolean not = false;
    private int oldOracleJoinSyntax = 0;

    public InExpression() {
    }

    public InExpression(Expression leftExpression, ItemsList itemsList) {
        setLeftExpression(leftExpression);
        setRightItemsList(itemsList);
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.SupportsOldOracleJoinSyntax
    public void setOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        this.oldOracleJoinSyntax = oldOracleJoinSyntax;
        if (oldOracleJoinSyntax < 0 || oldOracleJoinSyntax > 1) {
            throw new IllegalArgumentException("unexpected join type for oracle found with IN (type=" + oldOracleJoinSyntax + ")");
        }
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.SupportsOldOracleJoinSyntax
    public int getOldOracleJoinSyntax() {
        return this.oldOracleJoinSyntax;
    }

    public ItemsList getRightItemsList() {
        return this.rightItemsList;
    }

    public Expression getLeftExpression() {
        return this.leftExpression;
    }

    public final void setRightItemsList(ItemsList list) {
        this.rightItemsList = list;
    }

    public final void setLeftExpression(Expression expression) {
        this.leftExpression = expression;
    }

    public boolean isNot() {
        return this.not;
    }

    public void setNot(boolean b) {
        this.not = b;
    }

    public ItemsList getLeftItemsList() {
        return this.leftItemsList;
    }

    public void setLeftItemsList(ItemsList leftItemsList) {
        this.leftItemsList = leftItemsList;
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    private String getLeftExpressionString() {
        return this.leftExpression + (this.oldOracleJoinSyntax == 1 ? "(+)" : "");
    }

    public String toString() {
        return (this.leftExpression == null ? this.leftItemsList : getLeftExpressionString()) + SymbolConstants.SPACE_SYMBOL + (this.not ? "NOT " : "") + "IN " + this.rightItemsList + "";
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.SupportsOldOracleJoinSyntax
    public int getOraclePriorPosition() {
        return 0;
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.SupportsOldOracleJoinSyntax
    public void setOraclePriorPosition(int priorPosition) {
        if (priorPosition != 0) {
            throw new IllegalArgumentException("unexpected prior for oracle found");
        }
    }
}
