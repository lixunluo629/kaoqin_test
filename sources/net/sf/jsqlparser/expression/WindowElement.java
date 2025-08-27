package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/WindowElement.class */
public class WindowElement {
    private Type type;
    private WindowOffset offset;
    private WindowRange range;

    /* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/WindowElement$Type.class */
    public enum Type {
        ROWS,
        RANGE
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public WindowOffset getOffset() {
        return this.offset;
    }

    public void setOffset(WindowOffset offset) {
        this.offset = offset;
    }

    public WindowRange getRange() {
        return this.range;
    }

    public void setRange(WindowRange range) {
        this.range = range;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder(this.type.toString());
        if (this.offset != null) {
            buffer.append(this.offset.toString());
        } else if (this.range != null) {
            buffer.append(this.range.toString());
        }
        return buffer.toString();
    }
}
