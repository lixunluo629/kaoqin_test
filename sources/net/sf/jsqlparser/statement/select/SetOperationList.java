package net.sf.jsqlparser.statement.select;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/SetOperationList.class */
public class SetOperationList implements SelectBody {
    private List<PlainSelect> plainSelects;
    private List<SetOperation> operations;
    private List<OrderByElement> orderByElements;
    private Limit limit;

    /* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/SetOperationList$SetOperationType.class */
    public enum SetOperationType {
        INTERSECT,
        EXCEPT,
        MINUS,
        UNION
    }

    @Override // net.sf.jsqlparser.statement.select.SelectBody
    public void accept(SelectVisitor selectVisitor) {
        selectVisitor.visit(this);
    }

    public List<OrderByElement> getOrderByElements() {
        return this.orderByElements;
    }

    public List<PlainSelect> getPlainSelects() {
        return this.plainSelects;
    }

    public List<SetOperation> getOperations() {
        return this.operations;
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public void setOpsAndSelects(List<PlainSelect> select, List<SetOperation> ops) {
        this.plainSelects = select;
        this.operations = ops;
        if (select.size() - 1 != ops.size()) {
            throw new IllegalArgumentException("list sizes are not valid");
        }
    }

    public Limit getLimit() {
        return this.limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < this.plainSelects.size(); i++) {
            if (i != 0) {
                buffer.append(SymbolConstants.SPACE_SYMBOL).append(this.operations.get(i - 1).toString()).append(SymbolConstants.SPACE_SYMBOL);
            }
            buffer.append("(").append(this.plainSelects.get(i).toString()).append(")");
        }
        if (this.orderByElements != null) {
            buffer.append(PlainSelect.orderByToString(this.orderByElements));
        }
        if (this.limit != null) {
            buffer.append(this.limit.toString());
        }
        return buffer.toString();
    }
}
