package net.sf.jsqlparser.expression.operators.relational;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.ExpressionVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/EqualsTo.class */
public class EqualsTo extends OldOracleJoinBinaryExpression {
    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override // net.sf.jsqlparser.expression.BinaryExpression
    public String getStringExpression() {
        return SymbolConstants.EQUAL_SYMBOL;
    }
}
