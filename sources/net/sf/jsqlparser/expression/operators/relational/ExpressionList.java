package net.sf.jsqlparser.expression.operators.relational;

import java.util.List;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/operators/relational/ExpressionList.class */
public class ExpressionList implements ItemsList {
    private List<Expression> expressions;

    public ExpressionList() {
    }

    public ExpressionList(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public List<Expression> getExpressions() {
        return this.expressions;
    }

    public void setExpressions(List<Expression> list) {
        this.expressions = list;
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsList
    public void accept(ItemsListVisitor itemsListVisitor) {
        itemsListVisitor.visit(this);
    }

    public String toString() {
        return PlainSelect.getStringList(this.expressions, true, true);
    }
}
