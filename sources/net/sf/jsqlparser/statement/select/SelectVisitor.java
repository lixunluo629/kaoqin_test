package net.sf.jsqlparser.statement.select;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/SelectVisitor.class */
public interface SelectVisitor {
    void visit(PlainSelect plainSelect);

    void visit(SetOperationList setOperationList);

    void visit(WithItem withItem);
}
