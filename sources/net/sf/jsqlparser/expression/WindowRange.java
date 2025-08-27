package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/WindowRange.class */
public class WindowRange {
    private WindowOffset start;
    private WindowOffset end;

    public WindowOffset getEnd() {
        return this.end;
    }

    public void setEnd(WindowOffset end) {
        this.end = end;
    }

    public WindowOffset getStart() {
        return this.start;
    }

    public void setStart(WindowOffset start) {
        this.start = start;
    }

    public String toString() {
        return " BETWEEN" + this.start + " AND" + this.end;
    }
}
