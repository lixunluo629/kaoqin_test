package net.sf.jsqlparser.util.deparser;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.update.Update;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/deparser/UpdateDeParser.class */
public class UpdateDeParser {
    private StringBuilder buffer;
    private ExpressionVisitor expressionVisitor;

    public UpdateDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
    }

    public StringBuilder getBuffer() {
        return this.buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(Update update) {
        this.buffer.append("UPDATE ").append(PlainSelect.getStringList(update.getTables(), true, false)).append(" SET ");
        for (int i = 0; i < update.getColumns().size(); i++) {
            Column column = update.getColumns().get(i);
            this.buffer.append(column.getFullyQualifiedName()).append(" = ");
            Expression expression = update.getExpressions().get(i);
            expression.accept(this.expressionVisitor);
            if (i < update.getColumns().size() - 1) {
                this.buffer.append(", ");
            }
        }
        if (update.getFromItem() != null) {
            this.buffer.append(" FROM ").append(update.getFromItem());
            if (update.getJoins() != null) {
                for (Join join : update.getJoins()) {
                    if (join.isSimple()) {
                        this.buffer.append(", ").append(join);
                    } else {
                        this.buffer.append(SymbolConstants.SPACE_SYMBOL).append(join);
                    }
                }
            }
        }
        if (update.getWhere() != null) {
            this.buffer.append(" WHERE ");
            update.getWhere().accept(this.expressionVisitor);
        }
    }

    public ExpressionVisitor getExpressionVisitor() {
        return this.expressionVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        this.expressionVisitor = visitor;
    }
}
