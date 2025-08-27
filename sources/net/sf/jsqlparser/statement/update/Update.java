package net.sf.jsqlparser.statement.update;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/update/Update.class */
public class Update implements Statement {
    private List<Table> tables;
    private Expression where;
    private List<Column> columns;
    private List<Expression> expressions;
    private FromItem fromItem;
    private List<Join> joins;

    @Override // net.sf.jsqlparser.statement.Statement
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public List<Table> getTables() {
        return this.tables;
    }

    public Expression getWhere() {
        return this.where;
    }

    public void setTables(List<Table> list) {
        this.tables = list;
    }

    public void setWhere(Expression expression) {
        this.where = expression;
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public List<Expression> getExpressions() {
        return this.expressions;
    }

    public void setColumns(List<Column> list) {
        this.columns = list;
    }

    public void setExpressions(List<Expression> list) {
        this.expressions = list;
    }

    public FromItem getFromItem() {
        return this.fromItem;
    }

    public void setFromItem(FromItem fromItem) {
        this.fromItem = fromItem;
    }

    public List<Join> getJoins() {
        return this.joins;
    }

    public void setJoins(List<Join> joins) {
        this.joins = joins;
    }

    public String toString() {
        StringBuilder b = new StringBuilder("UPDATE ");
        b.append(PlainSelect.getStringList(getTables(), true, false)).append(" SET ");
        for (int i = 0; i < getColumns().size(); i++) {
            if (i != 0) {
                b.append(", ");
            }
            b.append(this.columns.get(i)).append(" = ");
            b.append(this.expressions.get(i));
        }
        if (this.fromItem != null) {
            b.append(" FROM ").append(this.fromItem);
            if (this.joins != null) {
                for (Join join : this.joins) {
                    if (join.isSimple()) {
                        b.append(", ").append(join);
                    } else {
                        b.append(SymbolConstants.SPACE_SYMBOL).append(join);
                    }
                }
            }
        }
        if (this.where != null) {
            b.append(" WHERE ");
            b.append(this.where);
        }
        return b.toString();
    }
}
