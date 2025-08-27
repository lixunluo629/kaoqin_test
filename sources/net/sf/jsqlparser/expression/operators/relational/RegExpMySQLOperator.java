package net.sf.jsqlparser.expression.operators.relational;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/RegExpMySQLOperator.class */
public class RegExpMySQLOperator extends BinaryExpression {
    private RegExpMatchOperatorType operatorType;

    public RegExpMySQLOperator(RegExpMatchOperatorType operatorType) {
        if (operatorType == null) {
            throw new NullPointerException();
        }
        this.operatorType = operatorType;
    }

    public RegExpMatchOperatorType getOperatorType() {
        return this.operatorType;
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override // net.sf.jsqlparser.expression.BinaryExpression
    public String getStringExpression() {
        switch (this.operatorType) {
            case MATCH_CASESENSITIVE:
                return "REGEXP BINARY";
            case MATCH_CASEINSENSITIVE:
                return "REGEXP";
            default:
                return null;
        }
    }
}
