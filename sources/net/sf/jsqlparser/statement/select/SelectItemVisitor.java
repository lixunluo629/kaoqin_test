package net.sf.jsqlparser.statement.select;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/SelectItemVisitor.class */
public interface SelectItemVisitor {
    void visit(AllColumns allColumns);

    void visit(AllTableColumns allTableColumns);

    void visit(SelectExpressionItem selectExpressionItem);
}
