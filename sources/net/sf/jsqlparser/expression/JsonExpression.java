package net.sf.jsqlparser.expression;

import java.util.ArrayList;
import java.util.List;
import net.sf.jsqlparser.schema.Column;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/JsonExpression.class */
public class JsonExpression implements Expression {
    private Column column;
    private List<String> idents = new ArrayList();

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Column getColumn() {
        return this.column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public List<String> getIdents() {
        return this.idents;
    }

    public void setIdents(List<String> idents) {
        this.idents = idents;
    }

    public void addIdent(String ident) {
        this.idents.add(ident);
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(this.column.toString());
        for (String ident : this.idents) {
            b.append("->").append(ident);
        }
        return b.toString();
    }
}
