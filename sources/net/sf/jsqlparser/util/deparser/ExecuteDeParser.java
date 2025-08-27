package net.sf.jsqlparser.util.deparser;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.select.PlainSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/deparser/ExecuteDeParser.class */
public class ExecuteDeParser {
    private StringBuilder buffer;
    private ExpressionVisitor expressionVisitor;

    public ExecuteDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
    }

    public StringBuilder getBuffer() {
        return this.buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(Execute execute) {
        this.buffer.append("EXECUTE ").append(execute.getName());
        this.buffer.append(SymbolConstants.SPACE_SYMBOL).append(PlainSelect.getStringList(execute.getExprList().getExpressions(), true, false));
    }

    public ExpressionVisitor getExpressionVisitor() {
        return this.expressionVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        this.expressionVisitor = visitor;
    }
}
