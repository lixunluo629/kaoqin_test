package net.sf.jsqlparser.statement.create.view;

import java.util.List;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/create/view/CreateView.class */
public class CreateView implements Statement {
    private Table view;
    private SelectBody selectBody;
    private boolean orReplace = false;
    private List<String> columnNames = null;
    private boolean materialized = false;

    @Override // net.sf.jsqlparser.statement.Statement
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Table getView() {
        return this.view;
    }

    public void setView(Table view) {
        this.view = view;
    }

    public boolean isOrReplace() {
        return this.orReplace;
    }

    public void setOrReplace(boolean orReplace) {
        this.orReplace = orReplace;
    }

    public SelectBody getSelectBody() {
        return this.selectBody;
    }

    public void setSelectBody(SelectBody selectBody) {
        this.selectBody = selectBody;
    }

    public List<String> getColumnNames() {
        return this.columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public boolean isMaterialized() {
        return this.materialized;
    }

    public void setMaterialized(boolean materialized) {
        this.materialized = materialized;
    }

    public String toString() {
        StringBuilder sql = new StringBuilder("CREATE ");
        if (isOrReplace()) {
            sql.append("OR REPLACE ");
        }
        if (isMaterialized()) {
            sql.append("MATERIALIZED ");
        }
        sql.append("VIEW ");
        sql.append(this.view);
        if (this.columnNames != null) {
            sql.append(PlainSelect.getStringList(this.columnNames, true, true));
        }
        sql.append(" AS ").append(this.selectBody);
        return sql.toString();
    }
}
