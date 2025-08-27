package net.sf.jsqlparser.statement.create.table;

import java.util.List;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/create/table/ForeignKeyIndex.class */
public class ForeignKeyIndex extends NamedConstraint {
    private Table table;
    private List<String> referencedColumnNames;

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<String> getReferencedColumnNames() {
        return this.referencedColumnNames;
    }

    public void setReferencedColumnNames(List<String> referencedColumnNames) {
        this.referencedColumnNames = referencedColumnNames;
    }

    @Override // net.sf.jsqlparser.statement.create.table.NamedConstraint, net.sf.jsqlparser.statement.create.table.Index
    public String toString() {
        return super.toString() + " REFERENCES " + this.table + PlainSelect.getStringList(getReferencedColumnNames(), true, true);
    }
}
