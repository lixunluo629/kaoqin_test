package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.schema.Table;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/IntoTableVisitor.class */
public interface IntoTableVisitor {
    void visit(Table table);
}
