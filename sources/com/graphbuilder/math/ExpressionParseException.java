package com.graphbuilder.math;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/ExpressionParseException.class */
public class ExpressionParseException extends RuntimeException {
    private String descrip;
    private int index;

    public ExpressionParseException(String descrip, int index) {
        this.descrip = null;
        this.index = 0;
        this.descrip = descrip;
        this.index = index;
    }

    public String getDescription() {
        return this.descrip;
    }

    public int getIndex() {
        return this.index;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "(" + this.index + ") " + this.descrip;
    }
}
