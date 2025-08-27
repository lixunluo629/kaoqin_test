package net.sf.jsqlparser.expression;

import net.sf.jsqlparser.statement.create.table.ColDataType;
import org.hyperic.sigar.NetFlags;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/CastExpression.class */
public class CastExpression implements Expression {
    private Expression leftExpression;
    private ColDataType type;
    private boolean useCastKeyword = true;

    public ColDataType getType() {
        return this.type;
    }

    public void setType(ColDataType type) {
        this.type = type;
    }

    public Expression getLeftExpression() {
        return this.leftExpression;
    }

    public void setLeftExpression(Expression expression) {
        this.leftExpression = expression;
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public boolean isUseCastKeyword() {
        return this.useCastKeyword;
    }

    public void setUseCastKeyword(boolean useCastKeyword) {
        this.useCastKeyword = useCastKeyword;
    }

    public String toString() {
        if (this.useCastKeyword) {
            return "CAST(" + this.leftExpression + " AS " + this.type.toString() + ")";
        }
        return this.leftExpression + NetFlags.ANY_ADDR_V6 + this.type.toString();
    }
}
