package net.sf.jsqlparser.util.deparser;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/deparser/InsertDeParser.class */
public class InsertDeParser implements ItemsListVisitor {
    private StringBuilder buffer;
    private ExpressionVisitor expressionVisitor;
    private SelectVisitor selectVisitor;

    public InsertDeParser() {
    }

    public InsertDeParser(ExpressionVisitor expressionVisitor, SelectVisitor selectVisitor, StringBuilder buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
        this.selectVisitor = selectVisitor;
    }

    public StringBuilder getBuffer() {
        return this.buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(Insert insert) {
        this.buffer.append("INSERT INTO ");
        this.buffer.append(insert.getTable().getFullyQualifiedName());
        if (insert.getColumns() != null) {
            this.buffer.append(" (");
            Iterator<Column> iter = insert.getColumns().iterator();
            while (iter.hasNext()) {
                Column column = iter.next();
                this.buffer.append(column.getColumnName());
                if (iter.hasNext()) {
                    this.buffer.append(", ");
                }
            }
            this.buffer.append(")");
        }
        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this);
        }
        if (insert.getSelect() != null) {
            this.buffer.append(SymbolConstants.SPACE_SYMBOL);
            if (insert.isUseSelectBrackets()) {
                this.buffer.append("(");
            }
            if (insert.getSelect().getWithItemsList() != null) {
                this.buffer.append("WITH ");
                for (WithItem with : insert.getSelect().getWithItemsList()) {
                    with.accept(this.selectVisitor);
                }
                this.buffer.append(SymbolConstants.SPACE_SYMBOL);
            }
            insert.getSelect().getSelectBody().accept(this.selectVisitor);
            if (insert.isUseSelectBrackets()) {
                this.buffer.append(")");
            }
        }
        if (insert.isReturningAllColumns()) {
            this.buffer.append(" RETURNING *");
            return;
        }
        if (insert.getReturningExpressionList() != null) {
            this.buffer.append(" RETURNING ");
            Iterator<SelectExpressionItem> iter2 = insert.getReturningExpressionList().iterator();
            while (iter2.hasNext()) {
                this.buffer.append(iter2.next().toString());
                if (iter2.hasNext()) {
                    this.buffer.append(", ");
                }
            }
        }
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(ExpressionList expressionList) {
        this.buffer.append(" VALUES (");
        Iterator<Expression> iter = expressionList.getExpressions().iterator();
        while (iter.hasNext()) {
            Expression expression = iter.next();
            expression.accept(this.expressionVisitor);
            if (iter.hasNext()) {
                this.buffer.append(", ");
            }
        }
        this.buffer.append(")");
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(MultiExpressionList multiExprList) {
        this.buffer.append(" VALUES ");
        Iterator<ExpressionList> it = multiExprList.getExprList().iterator();
        while (it.hasNext()) {
            this.buffer.append("(");
            Iterator<Expression> iter = it.next().getExpressions().iterator();
            while (iter.hasNext()) {
                Expression expression = iter.next();
                expression.accept(this.expressionVisitor);
                if (iter.hasNext()) {
                    this.buffer.append(", ");
                }
            }
            this.buffer.append(")");
            if (it.hasNext()) {
                this.buffer.append(", ");
            }
        }
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(SubSelect subSelect) {
        subSelect.getSelectBody().accept(this.selectVisitor);
    }

    public ExpressionVisitor getExpressionVisitor() {
        return this.expressionVisitor;
    }

    public SelectVisitor getSelectVisitor() {
        return this.selectVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        this.expressionVisitor = visitor;
    }

    public void setSelectVisitor(SelectVisitor visitor) {
        this.selectVisitor = visitor;
    }
}
