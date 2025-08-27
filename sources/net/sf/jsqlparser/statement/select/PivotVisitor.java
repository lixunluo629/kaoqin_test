package net.sf.jsqlparser.statement.select;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/PivotVisitor.class */
public interface PivotVisitor {
    void visit(Pivot pivot);

    void visit(PivotXml pivotXml);
}
