package net.sf.jsqlparser.statement.execute;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/execute/Execute.class */
public class Execute implements Statement {
    private String name;
    private ExpressionList exprList;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExpressionList getExprList() {
        return this.exprList;
    }

    public void setExprList(ExpressionList exprList) {
        this.exprList = exprList;
    }

    @Override // net.sf.jsqlparser.statement.Statement
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public String toString() {
        return "EXECUTE " + this.name + SymbolConstants.SPACE_SYMBOL + PlainSelect.getStringList(this.exprList.getExpressions(), true, false);
    }
}
