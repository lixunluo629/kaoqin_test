package net.sf.jsqlparser.statement.select;

import java.util.Iterator;
import java.util.List;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/ValuesList.class */
public class ValuesList implements FromItem {
    private Alias alias;
    private MultiExpressionList multiExpressionList;
    private boolean noBrackets = false;
    private List<String> columnNames;

    public ValuesList() {
    }

    public ValuesList(MultiExpressionList multiExpressionList) {
        this.multiExpressionList = multiExpressionList;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void accept(FromItemVisitor fromItemVisitor) {
        fromItemVisitor.visit(this);
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public Alias getAlias() {
        return this.alias;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public Pivot getPivot() {
        return null;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void setPivot(Pivot pivot) {
    }

    public MultiExpressionList getMultiExpressionList() {
        return this.multiExpressionList;
    }

    public void setMultiExpressionList(MultiExpressionList multiExpressionList) {
        this.multiExpressionList = multiExpressionList;
    }

    public boolean isNoBrackets() {
        return this.noBrackets;
    }

    public void setNoBrackets(boolean noBrackets) {
        this.noBrackets = noBrackets;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("(VALUES ");
        Iterator<ExpressionList> it = getMultiExpressionList().getExprList().iterator();
        while (it.hasNext()) {
            b.append(PlainSelect.getStringList(it.next().getExpressions(), true, !isNoBrackets()));
            if (it.hasNext()) {
                b.append(", ");
            }
        }
        b.append(")");
        if (this.alias != null) {
            b.append(this.alias.toString());
            if (this.columnNames != null) {
                b.append("(");
                Iterator<String> it2 = this.columnNames.iterator();
                while (it2.hasNext()) {
                    b.append(it2.next());
                    if (it2.hasNext()) {
                        b.append(", ");
                    }
                }
                b.append(")");
            }
        }
        return b.toString();
    }

    public List<String> getColumnNames() {
        return this.columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }
}
