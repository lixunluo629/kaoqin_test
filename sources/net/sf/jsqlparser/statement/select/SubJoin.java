package net.sf.jsqlparser.statement.select;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.Alias;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/SubJoin.class */
public class SubJoin implements FromItem {
    private FromItem left;
    private Join join;
    private Alias alias;
    private Pivot pivot;

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void accept(FromItemVisitor fromItemVisitor) {
        fromItemVisitor.visit(this);
    }

    public FromItem getLeft() {
        return this.left;
    }

    public void setLeft(FromItem l) {
        this.left = l;
    }

    public Join getJoin() {
        return this.join;
    }

    public void setJoin(Join j) {
        this.join = j;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public Pivot getPivot() {
        return this.pivot;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public Alias getAlias() {
        return this.alias;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public String toString() {
        return "(" + this.left + SymbolConstants.SPACE_SYMBOL + this.join + ")" + (this.pivot != null ? SymbolConstants.SPACE_SYMBOL + this.pivot : "") + (this.alias != null ? this.alias.toString() : "");
    }
}
