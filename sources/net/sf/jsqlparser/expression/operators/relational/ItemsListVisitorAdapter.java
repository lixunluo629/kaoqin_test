package net.sf.jsqlparser.expression.operators.relational;

import net.sf.jsqlparser.statement.select.SubSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/ItemsListVisitorAdapter.class */
public class ItemsListVisitorAdapter implements ItemsListVisitor {
    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(SubSelect subSelect) {
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(ExpressionList expressionList) {
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(MultiExpressionList multiExprList) {
        for (ExpressionList list : multiExprList.getExprList()) {
            visit(list);
        }
    }
}
