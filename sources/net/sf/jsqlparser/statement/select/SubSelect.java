package net.sf.jsqlparser.statement.select;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/SubSelect.class */
public class SubSelect implements FromItem, Expression, ItemsList {
    private SelectBody selectBody;
    private Alias alias;
    private boolean useBrackets = true;
    private Pivot pivot;

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void accept(FromItemVisitor fromItemVisitor) {
        fromItemVisitor.visit(this);
    }

    public SelectBody getSelectBody() {
        return this.selectBody;
    }

    public void setSelectBody(SelectBody body) {
        this.selectBody = body;
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
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
        return this.pivot;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    public boolean isUseBrackets() {
        return this.useBrackets;
    }

    public void setUseBrackets(boolean useBrackets) {
        this.useBrackets = useBrackets;
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsList
    public void accept(ItemsListVisitor itemsListVisitor) {
        itemsListVisitor.visit(this);
    }

    public String toString() {
        return (this.useBrackets ? "(" : "") + this.selectBody + (this.useBrackets ? ")" : "") + (this.pivot != null ? SymbolConstants.SPACE_SYMBOL + this.pivot : "") + (this.alias != null ? this.alias.toString() : "");
    }
}
