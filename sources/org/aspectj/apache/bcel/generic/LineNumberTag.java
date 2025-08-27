package org.aspectj.apache.bcel.generic;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/LineNumberTag.class */
public class LineNumberTag extends Tag {
    private final int lineNumber;

    public LineNumberTag(int i) {
        this.lineNumber = i;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String toString() {
        return "line " + this.lineNumber;
    }

    public boolean equals(Object obj) {
        return (obj instanceof LineNumberTag) && this.lineNumber == ((LineNumberTag) obj).lineNumber;
    }

    public int hashCode() {
        return this.lineNumber;
    }
}
