package org.aspectj.weaver;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Position.class */
public class Position implements IHasPosition {
    private int start;
    private int end;

    public Position(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override // org.aspectj.weaver.IHasPosition
    public int getEnd() {
        return this.end;
    }

    @Override // org.aspectj.weaver.IHasPosition
    public int getStart() {
        return this.start;
    }
}
