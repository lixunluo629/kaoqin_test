package net.sf.jsqlparser.statement.select;

import java.util.List;
import net.sf.jsqlparser.schema.Column;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/Pivot.class */
public class Pivot {
    private List<FunctionItem> functionItems;
    private List<Column> forColumns;
    private List<SelectExpressionItem> singleInItems;
    private List<ExpressionListItem> multiInItems;

    public void accept(PivotVisitor pivotVisitor) {
        pivotVisitor.visit(this);
    }

    public List<SelectExpressionItem> getSingleInItems() {
        return this.singleInItems;
    }

    public void setSingleInItems(List<SelectExpressionItem> singleInItems) {
        this.singleInItems = singleInItems;
    }

    public List<ExpressionListItem> getMultiInItems() {
        return this.multiInItems;
    }

    public void setMultiInItems(List<ExpressionListItem> multiInItems) {
        this.multiInItems = multiInItems;
    }

    public List<FunctionItem> getFunctionItems() {
        return this.functionItems;
    }

    public void setFunctionItems(List<FunctionItem> functionItems) {
        this.functionItems = functionItems;
    }

    public List<Column> getForColumns() {
        return this.forColumns;
    }

    public void setForColumns(List<Column> forColumns) {
        this.forColumns = forColumns;
    }

    public List<?> getInItems() {
        return this.singleInItems == null ? this.multiInItems : this.singleInItems;
    }

    public String toString() {
        return "PIVOT (" + PlainSelect.getStringList(this.functionItems) + " FOR " + PlainSelect.getStringList(this.forColumns, true, this.forColumns != null && this.forColumns.size() > 1) + " IN " + PlainSelect.getStringList(getInItems(), true, true) + ")";
    }
}
