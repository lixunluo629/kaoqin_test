package net.sf.jsqlparser.util;

import java.util.LinkedList;
import java.util.List;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/ConnectExpressionsVisitor.class */
public abstract class ConnectExpressionsVisitor implements SelectVisitor, SelectItemVisitor {
    private String alias;
    private final List<SelectExpressionItem> itemsExpr;

    protected abstract BinaryExpression createBinaryExpression();

    public ConnectExpressionsVisitor() {
        this.alias = "expr";
        this.itemsExpr = new LinkedList();
    }

    public ConnectExpressionsVisitor(String alias) {
        this.alias = "expr";
        this.itemsExpr = new LinkedList();
        this.alias = alias;
    }

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(PlainSelect plainSelect) {
        for (SelectItem item : plainSelect.getSelectItems()) {
            item.accept(this);
        }
        if (this.itemsExpr.size() > 1) {
            BinaryExpression binExpr = createBinaryExpression();
            binExpr.setLeftExpression(this.itemsExpr.get(0).getExpression());
            for (int i = 1; i < this.itemsExpr.size() - 1; i++) {
                binExpr.setRightExpression(this.itemsExpr.get(i).getExpression());
                BinaryExpression binExpr2 = createBinaryExpression();
                binExpr2.setLeftExpression(binExpr);
                binExpr = binExpr2;
            }
            binExpr.setRightExpression(this.itemsExpr.get(this.itemsExpr.size() - 1).getExpression());
            SelectExpressionItem sei = new SelectExpressionItem();
            sei.setExpression(binExpr);
            plainSelect.getSelectItems().clear();
            plainSelect.getSelectItems().add(sei);
        }
        ((SelectExpressionItem) plainSelect.getSelectItems().get(0)).setAlias(new Alias(this.alias));
    }

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(SetOperationList setOpList) {
        for (PlainSelect select : setOpList.getPlainSelects()) {
            select.accept(this);
        }
    }

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(WithItem withItem) {
    }

    @Override // net.sf.jsqlparser.statement.select.SelectItemVisitor
    public void visit(AllTableColumns allTableColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // net.sf.jsqlparser.statement.select.SelectItemVisitor
    public void visit(AllColumns allColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // net.sf.jsqlparser.statement.select.SelectItemVisitor
    public void visit(SelectExpressionItem selectExpressionItem) {
        this.itemsExpr.add(selectExpressionItem);
    }
}
