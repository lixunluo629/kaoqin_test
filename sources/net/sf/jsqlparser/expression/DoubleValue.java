package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/DoubleValue.class */
public class DoubleValue implements Expression {
    private double value;
    private String stringValue;

    public DoubleValue(String value) {
        String val = value;
        val = val.charAt(0) == '+' ? val.substring(1) : val;
        this.value = Double.parseDouble(val);
        this.stringValue = val;
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double d) {
        this.value = d;
    }

    public String toString() {
        return this.stringValue;
    }
}
