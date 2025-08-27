package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/LongValue.class */
public class LongValue implements Expression {
    private long value;
    private String stringValue;

    public LongValue(String value) {
        String val = value;
        val = val.charAt(0) == '+' ? val.substring(1) : val;
        this.value = Long.parseLong(val);
        this.stringValue = val;
    }

    public LongValue(long value) {
        this.value = value;
        this.stringValue = String.valueOf(value);
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long d) {
        this.value = d;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public void setStringValue(String string) {
        this.stringValue = string;
    }

    public String toString() {
        return getStringValue();
    }
}
