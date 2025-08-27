package net.sf.jsqlparser.statement.insert;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/insert/Insert.class */
public class Insert implements Statement {
    private Table table;
    private List<Column> columns;
    private ItemsList itemsList;
    private Select select;
    private boolean useValues = true;
    private boolean useSelectBrackets = true;
    private boolean returningAllColumns = false;
    private List<SelectExpressionItem> returningExpressionList = null;

    @Override // net.sf.jsqlparser.statement.Statement
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table name) {
        this.table = name;
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public void setColumns(List<Column> list) {
        this.columns = list;
    }

    public ItemsList getItemsList() {
        return this.itemsList;
    }

    public void setItemsList(ItemsList list) {
        this.itemsList = list;
    }

    public boolean isUseValues() {
        return this.useValues;
    }

    public void setUseValues(boolean useValues) {
        this.useValues = useValues;
    }

    public boolean isReturningAllColumns() {
        return this.returningAllColumns;
    }

    public void setReturningAllColumns(boolean returningAllColumns) {
        this.returningAllColumns = returningAllColumns;
    }

    public List<SelectExpressionItem> getReturningExpressionList() {
        return this.returningExpressionList;
    }

    public void setReturningExpressionList(List<SelectExpressionItem> returningExpressionList) {
        this.returningExpressionList = returningExpressionList;
    }

    public Select getSelect() {
        return this.select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    public boolean isUseSelectBrackets() {
        return this.useSelectBrackets;
    }

    public void setUseSelectBrackets(boolean useSelectBrackets) {
        this.useSelectBrackets = useSelectBrackets;
    }

    public String toString() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(this.table).append(SymbolConstants.SPACE_SYMBOL);
        if (this.columns != null) {
            sql.append(PlainSelect.getStringList(this.columns, true, true)).append(SymbolConstants.SPACE_SYMBOL);
        }
        if (this.useValues) {
            sql.append("VALUES ");
        }
        if (this.itemsList != null) {
            sql.append(this.itemsList);
        }
        if (this.useSelectBrackets) {
            sql.append("(");
        }
        if (this.select != null) {
            sql.append(this.select);
        }
        if (this.useSelectBrackets) {
            sql.append(")");
        }
        if (isReturningAllColumns()) {
            sql.append(" RETURNING *");
        } else if (getReturningExpressionList() != null) {
            sql.append(" RETURNING ").append(PlainSelect.getStringList(getReturningExpressionList(), true, false));
        }
        return sql.toString();
    }
}
