package net.sf.jsqlparser.expression.operators.relational;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.sf.jsqlparser.expression.Expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/MultiExpressionList.class */
public class MultiExpressionList implements ItemsList {
    private List<ExpressionList> exprList = new ArrayList();

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsList
    public void accept(ItemsListVisitor itemsListVisitor) {
        itemsListVisitor.visit(this);
    }

    public List<ExpressionList> getExprList() {
        return this.exprList;
    }

    public void addExpressionList(ExpressionList el) {
        if (!this.exprList.isEmpty() && this.exprList.get(0).getExpressions().size() != el.getExpressions().size()) {
            throw new IllegalArgumentException("different count of parameters");
        }
        this.exprList.add(el);
    }

    public void addExpressionList(List<Expression> list) {
        addExpressionList(new ExpressionList(list));
    }

    public void addExpressionList(Expression expr) {
        addExpressionList(new ExpressionList(Arrays.asList(expr)));
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        Iterator<ExpressionList> it = this.exprList.iterator();
        while (it.hasNext()) {
            b.append(it.next().toString());
            if (it.hasNext()) {
                b.append(", ");
            }
        }
        return b.toString();
    }
}
