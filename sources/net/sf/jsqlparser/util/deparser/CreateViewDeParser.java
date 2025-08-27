package net.sf.jsqlparser.util.deparser;

import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/deparser/CreateViewDeParser.class */
public class CreateViewDeParser {
    private StringBuilder buffer;

    public CreateViewDeParser(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(CreateView createView) {
        this.buffer.append("CREATE ");
        if (createView.isOrReplace()) {
            this.buffer.append("OR REPLACE ");
        }
        if (createView.isMaterialized()) {
            this.buffer.append("MATERIALIZED ");
        }
        this.buffer.append("VIEW ").append(createView.getView().getFullyQualifiedName());
        if (createView.getColumnNames() != null) {
            this.buffer.append(PlainSelect.getStringList(createView.getColumnNames(), true, true));
        }
        this.buffer.append(" AS ");
        this.buffer.append(createView.getSelectBody().toString());
    }

    public StringBuilder getBuffer() {
        return this.buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }
}
