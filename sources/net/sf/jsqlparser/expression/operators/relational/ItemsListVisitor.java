package net.sf.jsqlparser.expression.operators.relational;

import net.sf.jsqlparser.statement.select.SubSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/ItemsListVisitor.class */
public interface ItemsListVisitor {
    void visit(SubSelect subSelect);

    void visit(ExpressionList expressionList);

    void visit(MultiExpressionList multiExpressionList);
}
