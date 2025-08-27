package net.sf.jsqlparser.statement.replace;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/replace/Replace.class */
public class Replace implements Statement {
    private Table table;
    private List<Column> columns;
    private ItemsList itemsList;
    private List<Expression> expressions;
    private boolean useValues = true;

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

    public ItemsList getItemsList() {
        return this.itemsList;
    }

    public void setColumns(List<Column> list) {
        this.columns = list;
    }

    public void setItemsList(ItemsList list) {
        this.itemsList = list;
    }

    public List<Expression> getExpressions() {
        return this.expressions;
    }

    public void setExpressions(List<Expression> list) {
        this.expressions = list;
    }

    public boolean isUseValues() {
        return this.useValues;
    }

    public void setUseValues(boolean useValues) {
        this.useValues = useValues;
    }

    public String toString() {
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE ").append(this.table);
        if (this.expressions != null && this.columns != null) {
            sql.append(" SET ");
            int i = 0;
            int s = this.columns.size();
            while (i < s) {
                sql.append(this.columns.get(i)).append(SymbolConstants.EQUAL_SYMBOL).append(this.expressions.get(i));
                sql.append(i < s - 1 ? ", " : "");
                i++;
            }
        } else if (this.columns != null) {
            sql.append(SymbolConstants.SPACE_SYMBOL).append(PlainSelect.getStringList(this.columns, true, true));
        }
        if (this.itemsList != null) {
            if (this.useValues) {
                sql.append(" VALUES");
            }
            sql.append(SymbolConstants.SPACE_SYMBOL).append(this.itemsList);
        }
        return sql.toString();
    }
}
