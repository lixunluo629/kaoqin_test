package net.sf.jsqlparser.util.deparser;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.Index;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/deparser/CreateIndexDeParser.class */
public class CreateIndexDeParser {
    private StringBuilder buffer;

    public CreateIndexDeParser(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(CreateIndex createIndex) {
        Index index = createIndex.getIndex();
        this.buffer.append("CREATE ");
        if (index.getType() != null) {
            this.buffer.append(index.getType());
            this.buffer.append(SymbolConstants.SPACE_SYMBOL);
        }
        this.buffer.append("INDEX ");
        this.buffer.append(index.getName());
        this.buffer.append(" ON ");
        this.buffer.append(createIndex.getTable().getFullyQualifiedName());
        if (index.getColumnsNames() != null) {
            this.buffer.append(" (");
            Iterator iter = index.getColumnsNames().iterator();
            while (iter.hasNext()) {
                String columnName = iter.next();
                this.buffer.append(columnName);
                if (iter.hasNext()) {
                    this.buffer.append(", ");
                }
            }
            this.buffer.append(")");
        }
    }

    public StringBuilder getBuffer() {
        return this.buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }
}
