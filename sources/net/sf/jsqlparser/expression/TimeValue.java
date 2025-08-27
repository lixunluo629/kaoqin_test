package net.sf.jsqlparser.expression;

import java.sql.Time;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/TimeValue.class */
public class TimeValue implements Expression {
    private Time value;

    public TimeValue(String value) {
        this.value = Time.valueOf(value.substring(1, value.length() - 1));
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Time getValue() {
        return this.value;
    }

    public void setValue(Time d) {
        this.value = d;
    }

    public String toString() {
        return "{t '" + this.value + "'}";
    }
}
