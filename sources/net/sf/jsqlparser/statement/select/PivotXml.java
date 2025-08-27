package net.sf.jsqlparser.statement.select;

import java.util.List;
import net.sf.jsqlparser.schema.Column;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/PivotXml.class */
public class PivotXml extends Pivot {
    private SelectBody inSelect;
    private boolean inAny = false;

    @Override // net.sf.jsqlparser.statement.select.Pivot
    public void accept(PivotVisitor pivotVisitor) {
        pivotVisitor.visit(this);
    }

    public SelectBody getInSelect() {
        return this.inSelect;
    }

    public void setInSelect(SelectBody inSelect) {
        this.inSelect = inSelect;
    }

    public boolean isInAny() {
        return this.inAny;
    }

    public void setInAny(boolean inAny) {
        this.inAny = inAny;
    }

    @Override // net.sf.jsqlparser.statement.select.Pivot
    public String toString() {
        List<Column> forColumns = getForColumns();
        String in = this.inAny ? "ANY" : this.inSelect == null ? PlainSelect.getStringList(getInItems()) : this.inSelect.toString();
        return "PIVOT XML (" + PlainSelect.getStringList(getFunctionItems()) + " FOR " + PlainSelect.getStringList(forColumns, true, forColumns != null && forColumns.size() > 1) + " IN (" + in + "))";
    }
}
