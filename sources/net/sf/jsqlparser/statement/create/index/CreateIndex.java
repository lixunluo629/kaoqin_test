package net.sf.jsqlparser.statement.create.index;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.create.table.Index;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/create/index/CreateIndex.class */
public class CreateIndex implements Statement {
    private Table table;
    private Index index;

    @Override // net.sf.jsqlparser.statement.Statement
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Index getIndex() {
        return this.index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("CREATE ");
        if (this.index.getType() != null) {
            buffer.append(this.index.getType());
            buffer.append(SymbolConstants.SPACE_SYMBOL);
        }
        buffer.append("INDEX ");
        buffer.append(this.index.getName());
        buffer.append(" ON ");
        buffer.append(this.table.getFullyQualifiedName());
        if (this.index.getColumnsNames() != null) {
            buffer.append(" (");
            Iterator iter = this.index.getColumnsNames().iterator();
            while (iter.hasNext()) {
                String columnName = iter.next();
                buffer.append(columnName);
                if (iter.hasNext()) {
                    buffer.append(", ");
                }
            }
            buffer.append(")");
        }
        return buffer.toString();
    }
}
