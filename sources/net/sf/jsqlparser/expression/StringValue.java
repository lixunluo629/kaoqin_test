package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/StringValue.class */
public class StringValue implements Expression {
    private String value;

    public StringValue(String escapedValue) {
        this.value = "";
        this.value = escapedValue.substring(1, escapedValue.length() - 1);
    }

    public String getValue() {
        return this.value;
    }

    public String getNotExcapedValue() {
        StringBuilder buffer = new StringBuilder(this.value);
        int index = 0;
        int deletesNum = 0;
        while (true) {
            int index2 = this.value.indexOf("''", index);
            if (index2 != -1) {
                buffer.deleteCharAt(index2 - deletesNum);
                index = index2 + 2;
                deletesNum++;
            } else {
                return buffer.toString();
            }
        }
    }

    public void setValue(String string) {
        this.value = string;
    }

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String toString() {
        return "'" + this.value + "'";
    }
}
