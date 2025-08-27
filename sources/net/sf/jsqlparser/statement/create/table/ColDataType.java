package net.sf.jsqlparser.statement.create.table;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/create/table/ColDataType.class */
public class ColDataType {
    private String dataType;
    private List<String> argumentsStringList;
    private String characterSet;

    public List<String> getArgumentsStringList() {
        return this.argumentsStringList;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setArgumentsStringList(List<String> list) {
        this.argumentsStringList = list;
    }

    public void setDataType(String string) {
        this.dataType = string;
    }

    public String getCharacterSet() {
        return this.characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public String toString() {
        return this.dataType + (this.argumentsStringList != null ? SymbolConstants.SPACE_SYMBOL + PlainSelect.getStringList(this.argumentsStringList, true, true) : "") + (this.characterSet != null ? " CHARACTER SET " + this.characterSet : "");
    }
}
