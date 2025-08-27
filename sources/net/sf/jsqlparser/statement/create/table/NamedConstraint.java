package net.sf.jsqlparser.statement.create.table;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/create/table/NamedConstraint.class */
public class NamedConstraint extends Index {
    @Override // net.sf.jsqlparser.statement.create.table.Index
    public String toString() {
        return (getName() != null ? "CONSTRAINT " + getName() + SymbolConstants.SPACE_SYMBOL : "") + getType() + SymbolConstants.SPACE_SYMBOL + PlainSelect.getStringList(getColumnsNames(), true, true);
    }
}
