package net.sf.jsqlparser.statement.drop;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/drop/Drop.class */
public class Drop implements Statement {
    private String type;
    private String name;
    private List<String> parameters;

    @Override // net.sf.jsqlparser.statement.Statement
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public String getName() {
        return this.name;
    }

    public List<String> getParameters() {
        return this.parameters;
    }

    public String getType() {
        return this.type;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void setParameters(List<String> list) {
        this.parameters = list;
    }

    public void setType(String string) {
        this.type = string;
    }

    public String toString() {
        String sql = "DROP " + this.type + SymbolConstants.SPACE_SYMBOL + this.name;
        if (this.parameters != null && this.parameters.size() > 0) {
            sql = sql + SymbolConstants.SPACE_SYMBOL + PlainSelect.getStringList(this.parameters);
        }
        return sql;
    }
}
