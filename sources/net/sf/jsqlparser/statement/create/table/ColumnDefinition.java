package net.sf.jsqlparser.statement.create.table;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/create/table/ColumnDefinition.class */
public class ColumnDefinition {
    private String columnName;
    private ColDataType colDataType;
    private List<String> columnSpecStrings;

    public List<String> getColumnSpecStrings() {
        return this.columnSpecStrings;
    }

    public void setColumnSpecStrings(List<String> list) {
        this.columnSpecStrings = list;
    }

    public ColDataType getColDataType() {
        return this.colDataType;
    }

    public void setColDataType(ColDataType type) {
        this.colDataType = type;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String string) {
        this.columnName = string;
    }

    public String toString() {
        return this.columnName + SymbolConstants.SPACE_SYMBOL + this.colDataType + (this.columnSpecStrings != null ? SymbolConstants.SPACE_SYMBOL + PlainSelect.getStringList(this.columnSpecStrings, false, false) : "");
    }
}
