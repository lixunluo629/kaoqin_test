package net.sf.jsqlparser.statement.select;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/WithItem.class */
public class WithItem implements SelectBody {
    private String name;
    private List<SelectItem> withItemList;
    private SelectBody selectBody;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SelectBody getSelectBody() {
        return this.selectBody;
    }

    public void setSelectBody(SelectBody selectBody) {
        this.selectBody = selectBody;
    }

    public List<SelectItem> getWithItemList() {
        return this.withItemList;
    }

    public void setWithItemList(List<SelectItem> withItemList) {
        this.withItemList = withItemList;
    }

    public String toString() {
        return this.name + (this.withItemList != null ? SymbolConstants.SPACE_SYMBOL + PlainSelect.getStringList(this.withItemList, true, true) : "") + " AS (" + this.selectBody + ")";
    }

    @Override // net.sf.jsqlparser.statement.select.SelectBody
    public void accept(SelectVisitor visitor) {
        visitor.visit(this);
    }
}
