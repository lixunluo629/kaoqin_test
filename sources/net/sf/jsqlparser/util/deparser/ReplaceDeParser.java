package net.sf.jsqlparser.util.deparser;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/deparser/ReplaceDeParser.class */
public class ReplaceDeParser implements ItemsListVisitor {
    private StringBuilder buffer;
    private ExpressionVisitor expressionVisitor;
    private SelectVisitor selectVisitor;

    public ReplaceDeParser() {
    }

    public ReplaceDeParser(ExpressionVisitor expressionVisitor, SelectVisitor selectVisitor, StringBuilder buffer) {
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

    public void deParse(Replace replace) {
        this.buffer.append("REPLACE ").append(replace.getTable().getFullyQualifiedName());
        if (replace.getItemsList() != null) {
            if (replace.getColumns() != null) {
                this.buffer.append(" (");
                for (int i = 0; i < replace.getColumns().size(); i++) {
                    Column column = replace.getColumns().get(i);
                    this.buffer.append(column.getFullyQualifiedName());
                    if (i < replace.getColumns().size() - 1) {
                        this.buffer.append(", ");
                    }
                }
                this.buffer.append(") ");
            } else {
                this.buffer.append(SymbolConstants.SPACE_SYMBOL);
            }
        } else {
            this.buffer.append(" SET ");
            for (int i2 = 0; i2 < replace.getColumns().size(); i2++) {
                Column column2 = replace.getColumns().get(i2);
                this.buffer.append(column2.getFullyQualifiedName()).append(SymbolConstants.EQUAL_SYMBOL);
                Expression expression = replace.getExpressions().get(i2);
                expression.accept(this.expressionVisitor);
                if (i2 < replace.getColumns().size() - 1) {
                    this.buffer.append(", ");
                }
            }
        }
        if (replace.getItemsList() != null) {
            if (replace.isUseValues()) {
                this.buffer.append(" VALUES");
            }
            this.buffer.append(replace.getItemsList());
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

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(MultiExpressionList multiExprList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
