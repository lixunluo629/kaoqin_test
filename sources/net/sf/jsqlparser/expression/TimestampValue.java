package net.sf.jsqlparser.expression;

import java.sql.Timestamp;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/TimestampValue.class */
public class TimestampValue implements Expression {
    private Timestamp value;

    public TimestampValue(String value) {
        this.value = Timestamp.valueOf(value.substring(1, value.length() - 1));
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Timestamp getValue() {
        return this.value;
    }

    public void setValue(Timestamp d) {
        this.value = d;
    }

    public String toString() {
        return "{ts '" + this.value + "'}";
    }
}
