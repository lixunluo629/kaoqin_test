package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.schema.Table;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/AllTableColumns.class */
public class AllTableColumns implements SelectItem {
    private Table table;

    public AllTableColumns() {
    }

    public AllTableColumns(Table tableName) {
        this.table = tableName;
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @Override // net.sf.jsqlparser.statement.select.SelectItem
    public void accept(SelectItemVisitor selectItemVisitor) {
        selectItemVisitor.visit(this);
    }

    public String toString() {
        return this.table + ".*";
    }
}
