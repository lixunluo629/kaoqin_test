package net.sf.jsqlparser.statement.delete;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/delete/Delete.class */
public class Delete implements Statement {
    private Table table;
    private Expression where;

    @Override // net.sf.jsqlparser.statement.Statement
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Table getTable() {
        return this.table;
    }

    public Expression getWhere() {
        return this.where;
    }

    public void setTable(Table name) {
        this.table = name;
    }

    public void setWhere(Expression expression) {
        this.where = expression;
    }

    public String toString() {
        return "DELETE FROM " + this.table + (this.where != null ? " WHERE " + this.where : "");
    }
}
