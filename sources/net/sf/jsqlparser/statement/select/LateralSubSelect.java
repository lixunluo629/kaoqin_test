package net.sf.jsqlparser.statement.select;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.Alias;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/LateralSubSelect.class */
public class LateralSubSelect implements FromItem {
    private SubSelect subSelect;
    private Alias alias;
    private Pivot pivot;

    public void setSubSelect(SubSelect subSelect) {
        this.subSelect = subSelect;
    }

    public SubSelect getSubSelect() {
        return this.subSelect;
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
        return this.pivot;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItem
    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    public String toString() {
        return "LATERAL" + this.subSelect.toString() + (this.pivot != null ? SymbolConstants.SPACE_SYMBOL + this.pivot : "") + (this.alias != null ? this.alias.toString() : "");
    }
}
