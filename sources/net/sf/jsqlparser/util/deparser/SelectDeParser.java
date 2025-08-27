package net.sf.jsqlparser.util.deparser;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;
import java.util.List;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.OrderByVisitor;
import net.sf.jsqlparser.statement.select.Pivot;
import net.sf.jsqlparser.statement.select.PivotVisitor;
import net.sf.jsqlparser.statement.select.PivotXml;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.Top;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/deparser/SelectDeParser.class */
public class SelectDeParser implements SelectVisitor, OrderByVisitor, SelectItemVisitor, FromItemVisitor, PivotVisitor {
    private StringBuilder buffer;
    private ExpressionVisitor expressionVisitor;

    public SelectDeParser() {
    }

    public SelectDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
    }

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(PlainSelect plainSelect) {
        this.buffer.append("SELECT ");
        Top top = plainSelect.getTop();
        if (top != null) {
            this.buffer.append(top).append(SymbolConstants.SPACE_SYMBOL);
        }
        if (plainSelect.getDistinct() != null) {
            this.buffer.append("DISTINCT ");
            if (plainSelect.getDistinct().getOnSelectItems() != null) {
                this.buffer.append("ON (");
                Iterator<SelectItem> iter = plainSelect.getDistinct().getOnSelectItems().iterator();
                while (iter.hasNext()) {
                    SelectItem selectItem = iter.next();
                    selectItem.accept(this);
                    if (iter.hasNext()) {
                        this.buffer.append(", ");
                    }
                }
                this.buffer.append(") ");
            }
        }
        Iterator<SelectItem> iter2 = plainSelect.getSelectItems().iterator();
        while (iter2.hasNext()) {
            SelectItem selectItem2 = iter2.next();
            selectItem2.accept(this);
            if (iter2.hasNext()) {
                this.buffer.append(", ");
            }
        }
        if (plainSelect.getIntoTables() != null) {
            this.buffer.append(" INTO ");
            Iterator<Table> iter3 = plainSelect.getIntoTables().iterator();
            while (iter3.hasNext()) {
                visit(iter3.next());
                if (iter3.hasNext()) {
                    this.buffer.append(", ");
                }
            }
        }
        if (plainSelect.getFromItem() != null) {
            this.buffer.append(" FROM ");
            plainSelect.getFromItem().accept(this);
        }
        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                deparseJoin(join);
            }
        }
        if (plainSelect.getWhere() != null) {
            this.buffer.append(" WHERE ");
            plainSelect.getWhere().accept(this.expressionVisitor);
        }
        if (plainSelect.getOracleHierarchical() != null) {
            plainSelect.getOracleHierarchical().accept(this.expressionVisitor);
        }
        if (plainSelect.getGroupByColumnReferences() != null) {
            this.buffer.append(" GROUP BY ");
            Iterator<Expression> iter4 = plainSelect.getGroupByColumnReferences().iterator();
            while (iter4.hasNext()) {
                Expression columnReference = iter4.next();
                columnReference.accept(this.expressionVisitor);
                if (iter4.hasNext()) {
                    this.buffer.append(", ");
                }
            }
        }
        if (plainSelect.getHaving() != null) {
            this.buffer.append(" HAVING ");
            plainSelect.getHaving().accept(this.expressionVisitor);
        }
        if (plainSelect.getOrderByElements() != null) {
            deparseOrderBy(plainSelect.isOracleSiblings(), plainSelect.getOrderByElements());
        }
        if (plainSelect.getLimit() != null) {
            deparseLimit(plainSelect.getLimit());
        }
    }

    @Override // net.sf.jsqlparser.statement.select.OrderByVisitor
    public void visit(OrderByElement orderBy) {
        orderBy.getExpression().accept(this.expressionVisitor);
        if (!orderBy.isAsc()) {
            this.buffer.append(" DESC");
        } else if (orderBy.isAscDescPresent()) {
            this.buffer.append(" ASC");
        }
        if (orderBy.getNullOrdering() != null) {
            this.buffer.append(' ');
            this.buffer.append(orderBy.getNullOrdering() == OrderByElement.NullOrdering.NULLS_FIRST ? "NULLS FIRST" : "NULLS LAST");
        }
    }

    public void visit(Column column) {
        this.buffer.append(column.getFullyQualifiedName());
    }

    @Override // net.sf.jsqlparser.statement.select.SelectItemVisitor
    public void visit(AllTableColumns allTableColumns) {
        this.buffer.append(allTableColumns.getTable().getFullyQualifiedName()).append(".*");
    }

    @Override // net.sf.jsqlparser.statement.select.SelectItemVisitor
    public void visit(SelectExpressionItem selectExpressionItem) {
        selectExpressionItem.getExpression().accept(this.expressionVisitor);
        if (selectExpressionItem.getAlias() != null) {
            this.buffer.append(selectExpressionItem.getAlias().toString());
        }
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(SubSelect subSelect) {
        this.buffer.append("(");
        subSelect.getSelectBody().accept(this);
        this.buffer.append(")");
        Pivot pivot = subSelect.getPivot();
        if (pivot != null) {
            pivot.accept(this);
        }
        Alias alias = subSelect.getAlias();
        if (alias != null) {
            this.buffer.append(alias.toString());
        }
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(Table tableName) {
        this.buffer.append(tableName.getFullyQualifiedName());
        Pivot pivot = tableName.getPivot();
        if (pivot != null) {
            pivot.accept(this);
        }
        Alias alias = tableName.getAlias();
        if (alias != null) {
            this.buffer.append(alias);
        }
    }

    @Override // net.sf.jsqlparser.statement.select.PivotVisitor
    public void visit(Pivot pivot) {
        List<Column> forColumns = pivot.getForColumns();
        this.buffer.append(" PIVOT (").append(PlainSelect.getStringList(pivot.getFunctionItems())).append(" FOR ").append(PlainSelect.getStringList(forColumns, true, forColumns != null && forColumns.size() > 1)).append(" IN ").append(PlainSelect.getStringList(pivot.getInItems(), true, true)).append(")");
    }

    @Override // net.sf.jsqlparser.statement.select.PivotVisitor
    public void visit(PivotXml pivot) {
        List<Column> forColumns = pivot.getForColumns();
        this.buffer.append(" PIVOT XML (").append(PlainSelect.getStringList(pivot.getFunctionItems())).append(" FOR ").append(PlainSelect.getStringList(forColumns, true, forColumns != null && forColumns.size() > 1)).append(" IN (");
        if (pivot.isInAny()) {
            this.buffer.append("ANY");
        } else if (pivot.getInSelect() != null) {
            this.buffer.append(pivot.getInSelect());
        } else {
            this.buffer.append(PlainSelect.getStringList(pivot.getInItems()));
        }
        this.buffer.append("))");
    }

    public void deparseOrderBy(List<OrderByElement> orderByElements) {
        deparseOrderBy(false, orderByElements);
    }

    public void deparseOrderBy(boolean oracleSiblings, List<OrderByElement> orderByElements) {
        if (oracleSiblings) {
            this.buffer.append(" ORDER SIBLINGS BY ");
        } else {
            this.buffer.append(" ORDER BY ");
        }
        Iterator<OrderByElement> iter = orderByElements.iterator();
        while (iter.hasNext()) {
            OrderByElement orderByElement = iter.next();
            orderByElement.accept(this);
            if (iter.hasNext()) {
                this.buffer.append(", ");
            }
        }
    }

    public void deparseLimit(Limit limit) {
        if (limit.isRowCountJdbcParameter()) {
            this.buffer.append(" LIMIT ");
            this.buffer.append("?");
        } else if (limit.getRowCount() >= 0) {
            this.buffer.append(" LIMIT ");
            this.buffer.append(limit.getRowCount());
        } else if (limit.isLimitNull()) {
            this.buffer.append(" LIMIT NULL");
        }
        if (limit.isOffsetJdbcParameter()) {
            this.buffer.append(" OFFSET ?");
        } else if (limit.getOffset() != 0) {
            this.buffer.append(" OFFSET ").append(limit.getOffset());
        }
    }

    public StringBuilder getBuffer() {
        return this.buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public ExpressionVisitor getExpressionVisitor() {
        return this.expressionVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        this.expressionVisitor = visitor;
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(SubJoin subjoin) {
        this.buffer.append("(");
        subjoin.getLeft().accept(this);
        deparseJoin(subjoin.getJoin());
        this.buffer.append(")");
        if (subjoin.getPivot() != null) {
            subjoin.getPivot().accept(this);
        }
    }

    public void deparseJoin(Join join) {
        if (join.isSimple()) {
            this.buffer.append(", ");
        } else {
            if (join.isRight()) {
                this.buffer.append(" RIGHT");
            } else if (join.isNatural()) {
                this.buffer.append(" NATURAL");
            } else if (join.isFull()) {
                this.buffer.append(" FULL");
            } else if (join.isLeft()) {
                this.buffer.append(" LEFT");
            } else if (join.isCross()) {
                this.buffer.append(" CROSS");
            }
            if (join.isOuter()) {
                this.buffer.append(" OUTER");
            } else if (join.isInner()) {
                this.buffer.append(" INNER");
            }
            this.buffer.append(" JOIN ");
        }
        FromItem fromItem = join.getRightItem();
        fromItem.accept(this);
        if (join.getOnExpression() != null) {
            this.buffer.append(" ON ");
            join.getOnExpression().accept(this.expressionVisitor);
        }
        if (join.getUsingColumns() != null) {
            this.buffer.append(" USING (");
            Iterator<Column> iterator = join.getUsingColumns().iterator();
            while (iterator.hasNext()) {
                Column column = iterator.next();
                this.buffer.append(column.getFullyQualifiedName());
                if (iterator.hasNext()) {
                    this.buffer.append(", ");
                }
            }
            this.buffer.append(")");
        }
    }

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(SetOperationList list) {
        for (int i = 0; i < list.getPlainSelects().size(); i++) {
            if (i != 0) {
                this.buffer.append(' ').append(list.getOperations().get(i - 1)).append(' ');
            }
            this.buffer.append("(");
            PlainSelect plainSelect = list.getPlainSelects().get(i);
            plainSelect.accept(this);
            this.buffer.append(")");
        }
        if (list.getOrderByElements() != null) {
            deparseOrderBy(list.getOrderByElements());
        }
        if (list.getLimit() != null) {
            deparseLimit(list.getLimit());
        }
    }

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(WithItem withItem) {
        this.buffer.append(withItem.toString());
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(LateralSubSelect lateralSubSelect) {
        this.buffer.append(lateralSubSelect.toString());
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(ValuesList valuesList) {
        this.buffer.append(valuesList.toString());
    }

    @Override // net.sf.jsqlparser.statement.select.SelectItemVisitor
    public void visit(AllColumns allColumns) {
        this.buffer.append('*');
    }
}
