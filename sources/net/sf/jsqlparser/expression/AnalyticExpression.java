package net.sf.jsqlparser.expression;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/AnalyticExpression.class */
public class AnalyticExpression implements Expression {
    private ExpressionList partitionExpressionList;
    private List<OrderByElement> orderByElements;
    private String name;
    private Expression expression;
    private Expression offset;
    private Expression defaultValue;
    private boolean allColumns = false;
    private WindowElement windowElement;

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public List<OrderByElement> getOrderByElements() {
        return this.orderByElements;
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public ExpressionList getPartitionExpressionList() {
        return this.partitionExpressionList;
    }

    public void setPartitionExpressionList(ExpressionList partitionExpressionList) {
        this.partitionExpressionList = partitionExpressionList;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getOffset() {
        return this.offset;
    }

    public void setOffset(Expression offset) {
        this.offset = offset;
    }

    public Expression getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(Expression defaultValue) {
        this.defaultValue = defaultValue;
    }

    public WindowElement getWindowElement() {
        return this.windowElement;
    }

    public void setWindowElement(WindowElement windowElement) {
        this.windowElement = windowElement;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(this.name).append("(");
        if (this.expression != null) {
            b.append(this.expression.toString());
            if (this.offset != null) {
                b.append(", ").append(this.offset.toString());
                if (this.defaultValue != null) {
                    b.append(", ").append(this.defaultValue.toString());
                }
            }
        } else if (isAllColumns()) {
            b.append("*");
        }
        b.append(") OVER (");
        toStringPartitionBy(b);
        toStringOrderByElements(b);
        b.append(")");
        return b.toString();
    }

    public boolean isAllColumns() {
        return this.allColumns;
    }

    public void setAllColumns(boolean allColumns) {
        this.allColumns = allColumns;
    }

    private void toStringPartitionBy(StringBuilder b) {
        if (this.partitionExpressionList != null && !this.partitionExpressionList.getExpressions().isEmpty()) {
            b.append("PARTITION BY ");
            b.append(PlainSelect.getStringList(this.partitionExpressionList.getExpressions(), true, false));
            b.append(SymbolConstants.SPACE_SYMBOL);
        }
    }

    private void toStringOrderByElements(StringBuilder b) {
        if (this.orderByElements != null && !this.orderByElements.isEmpty()) {
            b.append("ORDER BY ");
            for (int i = 0; i < this.orderByElements.size(); i++) {
                if (i > 0) {
                    b.append(", ");
                }
                b.append(this.orderByElements.get(i).toString());
            }
            if (this.windowElement != null) {
                b.append(' ');
                b.append(this.windowElement);
            }
        }
    }
}
