package net.sf.jsqlparser.expression;

import java.sql.Date;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/DateValue.class */
public class DateValue implements Expression {
    private Date value;

    public DateValue(String value) {
        this.value = Date.valueOf(value.substring(1, value.length() - 1));
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Date getValue() {
        return this.value;
    }

    public void setValue(Date d) {
        this.value = d;
    }
}
