package net.sf.jsqlparser.statement.select;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;
import java.util.List;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/Select.class */
public class Select implements Statement {
    private SelectBody selectBody;
    private List<WithItem> withItemsList;

    @Override // net.sf.jsqlparser.statement.Statement
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public SelectBody getSelectBody() {
        return this.selectBody;
    }

    public void setSelectBody(SelectBody body) {
        this.selectBody = body;
    }

    public String toString() {
        StringBuilder retval = new StringBuilder();
        if (this.withItemsList != null && !this.withItemsList.isEmpty()) {
            retval.append("WITH ");
            Iterator<WithItem> iter = this.withItemsList.iterator();
            while (iter.hasNext()) {
                WithItem withItem = iter.next();
                retval.append(withItem);
                if (iter.hasNext()) {
                    retval.append(",");
                }
                retval.append(SymbolConstants.SPACE_SYMBOL);
            }
        }
        retval.append(this.selectBody);
        return retval.toString();
    }

    public List<WithItem> getWithItemsList() {
        return this.withItemsList;
    }

    public void setWithItemsList(List<WithItem> withItemsList) {
        this.withItemsList = withItemsList;
    }
}
