package net.sf.jsqlparser.statement.create.table;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/create/table/Index.class */
public class Index {
    private String type;
    private List<String> columnsNames;
    private String name;

    public List<String> getColumnsNames() {
        return this.columnsNames;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public void setColumnsNames(List<String> list) {
        this.columnsNames = list;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void setType(String string) {
        this.type = string;
    }

    public String toString() {
        return this.type + SymbolConstants.SPACE_SYMBOL + PlainSelect.getStringList(this.columnsNames, true, true) + (this.name != null ? SymbolConstants.SPACE_SYMBOL + this.name : "");
    }
}
