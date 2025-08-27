package net.sf.jsqlparser.statement.truncate;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/truncate/Truncate.class */
public class Truncate implements Statement {
    private Table table;

    @Override // net.sf.jsqlparser.statement.Statement
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String toString() {
        return "TRUNCATE TABLE " + this.table;
    }
}
