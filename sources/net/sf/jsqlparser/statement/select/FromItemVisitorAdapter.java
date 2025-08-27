package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.schema.Table;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/FromItemVisitorAdapter.class */
public class FromItemVisitorAdapter implements FromItemVisitor {
    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(Table table) {
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(SubSelect subSelect) {
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(SubJoin subjoin) {
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(LateralSubSelect lateralSubSelect) {
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(ValuesList valuesList) {
    }
}
