package com.graphbuilder.curve;

import com.graphbuilder.math.ExpressionParseException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/ControlStringParseException.class */
public class ControlStringParseException extends RuntimeException {
    private String descrip;
    private int fromIndex;
    private int toIndex;
    private ExpressionParseException epe;

    public ControlStringParseException(String descrip) {
        this.descrip = null;
        this.fromIndex = -1;
        this.toIndex = -1;
        this.epe = null;
        this.descrip = descrip;
    }

    public ControlStringParseException(String descrip, int index) {
        this.descrip = null;
        this.fromIndex = -1;
        this.toIndex = -1;
        this.epe = null;
        this.descrip = descrip;
        this.fromIndex = index;
        this.toIndex = index;
    }

    public ControlStringParseException(String descrip, int fromIndex, int toIndex) {
        this.descrip = null;
        this.fromIndex = -1;
        this.toIndex = -1;
        this.epe = null;
        this.descrip = descrip;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    public ControlStringParseException(String descrip, int fromIndex, int toIndex, ExpressionParseException epe) {
        this.descrip = null;
        this.fromIndex = -1;
        this.toIndex = -1;
        this.epe = null;
        this.descrip = descrip;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.epe = epe;
    }

    public int getFromIndex() {
        return this.fromIndex;
    }

    public int getToIndex() {
        return this.toIndex;
    }

    public String getDescription() {
        return this.descrip;
    }

    public ExpressionParseException getExpressionParseException() {
        return this.epe;
    }

    @Override // java.lang.Throwable
    public String toString() {
        String e = "";
        if (this.epe != null) {
            e = ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + this.epe.toString();
        }
        if (this.fromIndex == -1 && this.toIndex == -1) {
            return this.descrip + e;
        }
        if (this.fromIndex == this.toIndex) {
            return this.descrip + " : [" + this.toIndex + "]" + e;
        }
        return this.descrip + " : [" + this.fromIndex + ", " + this.toIndex + "]" + e;
    }
}
