package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.schema.Table;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/FromItemVisitor.class */
public interface FromItemVisitor {
    void visit(Table table);

    void visit(SubSelect subSelect);

    void visit(SubJoin subJoin);

    void visit(LateralSubSelect lateralSubSelect);

    void visit(ValuesList valuesList);
}
