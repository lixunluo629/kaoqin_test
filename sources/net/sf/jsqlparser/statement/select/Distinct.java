package net.sf.jsqlparser.statement.select;

import java.util.List;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/Distinct.class */
public class Distinct {
    private List<SelectItem> onSelectItems;

    public List<SelectItem> getOnSelectItems() {
        return this.onSelectItems;
    }

    public void setOnSelectItems(List<SelectItem> list) {
        this.onSelectItems = list;
    }

    public String toString() {
        String sql = "DISTINCT";
        if (this.onSelectItems != null && this.onSelectItems.size() > 0) {
            sql = sql + " ON (" + PlainSelect.getStringList(this.onSelectItems) + ")";
        }
        return sql;
    }
}
