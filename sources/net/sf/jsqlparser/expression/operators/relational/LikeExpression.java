package net.sf.jsqlparser.expression.operators.relational;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/LikeExpression.class */
public class LikeExpression extends BinaryExpression {
    private boolean not = false;
    private String escape = null;

    @Override // net.sf.jsqlparser.expression.BinaryExpression
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

    @Override // net.sf.jsqlparser.expression.BinaryExpression
    public String getStringExpression() {
        return (this.not ? "NOT " : "") + "LIKE";
    }

    @Override // net.sf.jsqlparser.expression.BinaryExpression
    public String toString() {
        String retval = super.toString();
        if (this.escape != null) {
            retval = retval + " ESCAPE '" + this.escape + "'";
        }
        return retval;
    }

    public String getEscape() {
        return this.escape;
    }

    public void setEscape(String escape) {
        this.escape = escape;
    }
}
