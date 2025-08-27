package net.sf.jsqlparser.expression.operators.relational;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.BinaryExpression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/OldOracleJoinBinaryExpression.class */
public abstract class OldOracleJoinBinaryExpression extends BinaryExpression implements SupportsOldOracleJoinSyntax {
    private int oldOracleJoinSyntax = 0;
    private int oraclePriorPosition = 0;

    @Override // net.sf.jsqlparser.expression.operators.relational.SupportsOldOracleJoinSyntax
    public void setOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        this.oldOracleJoinSyntax = oldOracleJoinSyntax;
        if (oldOracleJoinSyntax < 0 || oldOracleJoinSyntax > 2) {
            throw new IllegalArgumentException("unknown join type for oracle found (type=" + oldOracleJoinSyntax + ")");
        }
    }

    @Override // net.sf.jsqlparser.expression.BinaryExpression
    public String toString() {
        return (isNot() ? "NOT " : "") + (this.oraclePriorPosition == 1 ? "PRIOR " : "") + getLeftExpression() + (this.oldOracleJoinSyntax == 1 ? "(+)" : "") + SymbolConstants.SPACE_SYMBOL + getStringExpression() + SymbolConstants.SPACE_SYMBOL + (this.oraclePriorPosition == 2 ? "PRIOR " : "") + getRightExpression() + (this.oldOracleJoinSyntax == 2 ? "(+)" : "");
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.SupportsOldOracleJoinSyntax
    public int getOldOracleJoinSyntax() {
        return this.oldOracleJoinSyntax;
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.SupportsOldOracleJoinSyntax
    public int getOraclePriorPosition() {
        return this.oraclePriorPosition;
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.SupportsOldOracleJoinSyntax
    public void setOraclePriorPosition(int oraclePriorPosition) {
        this.oraclePriorPosition = oraclePriorPosition;
    }
}
