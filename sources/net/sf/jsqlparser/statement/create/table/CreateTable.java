package net.sf.jsqlparser.statement.create.table;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/create/table/CreateTable.class */
public class CreateTable implements Statement {
    private Table table;
    private boolean unlogged = false;
    private List<String> tableOptionsStrings;
    private List<ColumnDefinition> columnDefinitions;
    private List<Index> indexes;
    private Select select;

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

    public boolean isUnlogged() {
        return this.unlogged;
    }

    public void setUnlogged(boolean unlogged) {
        this.unlogged = unlogged;
    }

    public List<ColumnDefinition> getColumnDefinitions() {
        return this.columnDefinitions;
    }

    public void setColumnDefinitions(List<ColumnDefinition> list) {
        this.columnDefinitions = list;
    }

    public List<?> getTableOptionsStrings() {
        return this.tableOptionsStrings;
    }

    public void setTableOptionsStrings(List<String> list) {
        this.tableOptionsStrings = list;
    }

    public List<Index> getIndexes() {
        return this.indexes;
    }

    public void setIndexes(List<Index> list) {
        this.indexes = list;
    }

    public Select getSelect() {
        return this.select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    public String toString() {
        String sql;
        String sql2 = "CREATE " + (this.unlogged ? "UNLOGGED " : "") + "TABLE " + this.table;
        if (this.select != null) {
            sql = sql2 + " AS " + this.select.toString();
        } else {
            String sql3 = (sql2 + " (") + PlainSelect.getStringList(this.columnDefinitions, true, false);
            if (this.indexes != null && this.indexes.size() != 0) {
                sql3 = (sql3 + ", ") + PlainSelect.getStringList(this.indexes);
            }
            sql = sql3 + ")";
            String options = PlainSelect.getStringList(this.tableOptionsStrings, false, false);
            if (options != null && options.length() > 0) {
                sql = sql + SymbolConstants.SPACE_SYMBOL + options;
            }
        }
        return sql;
    }
}
