package net.sf.jsqlparser.statement.alter;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.create.table.ColDataType;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/alter/Alter.class */
public class Alter implements Statement {
    private Table table;
    private String columnName;
    private ColDataType dataType;

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public ColDataType getDataType() {
        return this.dataType;
    }

    public void setDataType(ColDataType dataType) {
        this.dataType = dataType;
    }

    @Override // net.sf.jsqlparser.statement.Statement
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public String toString() {
        return "ALTER TABLE " + this.table.getFullyQualifiedName() + " ADD COLUMN " + this.columnName + SymbolConstants.SPACE_SYMBOL + this.dataType.toString();
    }
}
