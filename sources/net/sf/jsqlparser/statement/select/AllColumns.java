package net.sf.jsqlparser.statement.select;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/AllColumns.class */
public class AllColumns implements SelectItem {
    @Override // net.sf.jsqlparser.statement.select.SelectItem
    public void accept(SelectItemVisitor selectItemVisitor) {
        selectItemVisitor.visit(this);
    }

    public String toString() {
        return "*";
    }
}
