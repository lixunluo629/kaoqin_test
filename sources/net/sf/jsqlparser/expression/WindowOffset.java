package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/WindowOffset.class */
public class WindowOffset {
    private Expression expression;
    private Type type;

    /* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/WindowOffset$Type.class */
    public enum Type {
        PRECEDING,
        FOLLOWING,
        CURRENT,
        EXPR
    }

    public Expression getExpression() {
        return this.expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        if (this.expression != null) {
            buffer.append(' ').append(this.expression);
            if (this.type != null) {
                buffer.append(' ');
                buffer.append(this.type);
            }
        } else {
            switch (this.type) {
                case PRECEDING:
                    buffer.append(" UNBOUNDED PRECEDING");
                    break;
                case FOLLOWING:
                    buffer.append(" UNBOUNDED FOLLOWING");
                    break;
                case CURRENT:
                    buffer.append(" CURRENT ROW");
                    break;
            }
        }
        return buffer.toString();
    }
}
