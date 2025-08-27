package net.sf.jsqlparser.expression;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/CaseExpression.class */
public class CaseExpression implements Expression {
    private Expression switchExpression;
    private List<Expression> whenClauses;
    private Expression elseExpression;

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Expression getSwitchExpression() {
        return this.switchExpression;
    }

    public void setSwitchExpression(Expression switchExpression) {
        this.switchExpression = switchExpression;
    }

    public Expression getElseExpression() {
        return this.elseExpression;
    }

    public void setElseExpression(Expression elseExpression) {
        this.elseExpression = elseExpression;
    }

    public List<Expression> getWhenClauses() {
        return this.whenClauses;
    }

    public void setWhenClauses(List<Expression> whenClauses) {
        this.whenClauses = whenClauses;
    }

    public String toString() {
        return "CASE " + (this.switchExpression != null ? this.switchExpression + SymbolConstants.SPACE_SYMBOL : "") + PlainSelect.getStringList(this.whenClauses, false, false) + SymbolConstants.SPACE_SYMBOL + (this.elseExpression != null ? "ELSE " + this.elseExpression + SymbolConstants.SPACE_SYMBOL : "") + "END";
    }
}
