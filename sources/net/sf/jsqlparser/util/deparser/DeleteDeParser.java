package net.sf.jsqlparser.util.deparser;

import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.statement.delete.Delete;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/deparser/DeleteDeParser.class */
public class DeleteDeParser {
    private StringBuilder buffer;
    private ExpressionVisitor expressionVisitor;

    public DeleteDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
    }

    public StringBuilder getBuffer() {
        return this.buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(Delete delete) {
        this.buffer.append("DELETE FROM ").append(delete.getTable().getFullyQualifiedName());
        if (delete.getWhere() != null) {
            this.buffer.append(" WHERE ");
            delete.getWhere().accept(this.expressionVisitor);
        }
    }

    public ExpressionVisitor getExpressionVisitor() {
        return this.expressionVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        this.expressionVisitor = visitor;
    }
}
