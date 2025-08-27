package net.sf.jsqlparser.schema;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/schema/Column.class */
public final class Column implements Expression, MultiPartName {
    private Table table;
    private String columnName;

    public Column() {
    }

    public Column(Table table, String columnName) {
        setTable(table);
        setColumnName(columnName);
    }

    public Column(String columnName) {
        this(null, columnName);
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String string) {
        this.columnName = string;
    }

    @Override // net.sf.jsqlparser.schema.MultiPartName
    public String getFullyQualifiedName() {
        StringBuilder fqn = new StringBuilder();
        if (this.table != null) {
            fqn.append(this.table.getFullyQualifiedName());
        }
        if (fqn.length() > 0) {
            fqn.append('.');
        }
        if (this.columnName != null) {
            fqn.append(this.columnName);
        }
        return fqn.toString();
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String toString() {
        return getFullyQualifiedName();
    }
}
