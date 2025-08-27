package net.sf.jsqlparser.util.deparser;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/deparser/CreateTableDeParser.class */
public class CreateTableDeParser {
    private StringBuilder buffer;

    public CreateTableDeParser(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(CreateTable createTable) {
        this.buffer.append("CREATE ");
        if (createTable.isUnlogged()) {
            this.buffer.append("UNLOGGED ");
        }
        this.buffer.append("TABLE ").append(createTable.getTable().getFullyQualifiedName());
        if (createTable.getSelect() != null) {
            this.buffer.append(" AS ").append(createTable.getSelect().toString());
            return;
        }
        if (createTable.getColumnDefinitions() != null) {
            this.buffer.append(" (");
            Iterator<ColumnDefinition> iter = createTable.getColumnDefinitions().iterator();
            while (iter.hasNext()) {
                ColumnDefinition columnDefinition = iter.next();
                this.buffer.append(columnDefinition.getColumnName());
                this.buffer.append(SymbolConstants.SPACE_SYMBOL);
                this.buffer.append(columnDefinition.getColDataType().toString());
                if (columnDefinition.getColumnSpecStrings() != null) {
                    for (String s : columnDefinition.getColumnSpecStrings()) {
                        this.buffer.append(SymbolConstants.SPACE_SYMBOL);
                        this.buffer.append(s);
                    }
                }
                if (iter.hasNext()) {
                    this.buffer.append(", ");
                }
            }
            if (createTable.getIndexes() != null) {
                for (Index index : createTable.getIndexes()) {
                    this.buffer.append(", ");
                    this.buffer.append(index.toString());
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
