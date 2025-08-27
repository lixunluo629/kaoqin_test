package com.itextpdf.kernel.geom;

import ch.qos.logback.classic.net.SyslogAppender;
import java.io.Serializable;
import java.util.Arrays;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/geom/Matrix.class */
public class Matrix implements Serializable {
    public static final int I11 = 0;
    public static final int I12 = 1;
    public static final int I13 = 2;
    public static final int I21 = 3;
    public static final int I22 = 4;
    public static final int I23 = 5;
    public static final int I31 = 6;
    public static final int I32 = 7;
    public static final int I33 = 8;
    private static final long serialVersionUID = 7434885566068528477L;
    private final float[] vals = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};

    public Matrix() {
    }

    public Matrix(float tx, float ty) {
        this.vals[6] = tx;
        this.vals[7] = ty;
    }

    public Matrix(float e11, float e12, float e13, float e21, float e22, float e23, float e31, float e32, float e33) {
        this.vals[0] = e11;
        this.vals[1] = e12;
        this.vals[2] = e13;
        this.vals[3] = e21;
        this.vals[4] = e22;
        this.vals[5] = e23;
        this.vals[6] = e31;
        this.vals[7] = e32;
        this.vals[8] = e33;
    }

    public Matrix(float a, float b, float c, float d, float e, float f) {
        this.vals[0] = a;
        this.vals[1] = b;
        this.vals[2] = 0.0f;
        this.vals[3] = c;
        this.vals[4] = d;
        this.vals[5] = 0.0f;
        this.vals[6] = e;
        this.vals[7] = f;
        this.vals[8] = 1.0f;
    }

    public float get(int index) {
        return this.vals[index];
    }

    public Matrix multiply(Matrix by) {
        Matrix rslt = new Matrix();
        float[] a = this.vals;
        float[] b = by.vals;
        float[] c = rslt.vals;
        c[0] = (a[0] * b[0]) + (a[1] * b[3]) + (a[2] * b[6]);
        c[1] = (a[0] * b[1]) + (a[1] * b[4]) + (a[2] * b[7]);
        c[2] = (a[0] * b[2]) + (a[1] * b[5]) + (a[2] * b[8]);
        c[3] = (a[3] * b[0]) + (a[4] * b[3]) + (a[5] * b[6]);
        c[4] = (a[3] * b[1]) + (a[4] * b[4]) + (a[5] * b[7]);
        c[5] = (a[3] * b[2]) + (a[4] * b[5]) + (a[5] * b[8]);
        c[6] = (a[6] * b[0]) + (a[7] * b[3]) + (a[8] * b[6]);
        c[7] = (a[6] * b[1]) + (a[7] * b[4]) + (a[8] * b[7]);
        c[8] = (a[6] * b[2]) + (a[7] * b[5]) + (a[8] * b[8]);
        return rslt;
    }

    public Matrix add(Matrix arg) {
        Matrix rslt = new Matrix();
        float[] a = this.vals;
        float[] b = arg.vals;
        float[] c = rslt.vals;
        c[0] = a[0] + b[0];
        c[1] = a[1] + b[1];
        c[2] = a[2] + b[2];
        c[3] = a[3] + b[3];
        c[4] = a[4] + b[4];
        c[5] = a[5] + b[5];
        c[6] = a[6] + b[6];
        c[7] = a[7] + b[7];
        c[8] = a[8] + b[8];
        return rslt;
    }

    public Matrix subtract(Matrix arg) {
        Matrix rslt = new Matrix();
        float[] a = this.vals;
        float[] b = arg.vals;
        float[] c = rslt.vals;
        c[0] = a[0] - b[0];
        c[1] = a[1] - b[1];
        c[2] = a[2] - b[2];
        c[3] = a[3] - b[3];
        c[4] = a[4] - b[4];
        c[5] = a[5] - b[5];
        c[6] = a[6] - b[6];
        c[7] = a[7] - b[7];
        c[8] = a[8] - b[8];
        return rslt;
    }

    public float getDeterminant() {
        return ((((((this.vals[0] * this.vals[4]) * this.vals[8]) + ((this.vals[1] * this.vals[5]) * this.vals[6])) + ((this.vals[2] * this.vals[3]) * this.vals[7])) - ((this.vals[0] * this.vals[5]) * this.vals[7])) - ((this.vals[1] * this.vals[3]) * this.vals[8])) - ((this.vals[2] * this.vals[4]) * this.vals[6]);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix)) {
            return false;
        }
        return Arrays.equals(this.vals, ((Matrix) obj).vals);
    }

    public int hashCode() {
        return Arrays.hashCode(this.vals);
    }

    public String toString() {
        return this.vals[0] + SyslogAppender.DEFAULT_STACKTRACE_PATTERN + this.vals[1] + SyslogAppender.DEFAULT_STACKTRACE_PATTERN + this.vals[2] + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + this.vals[3] + SyslogAppender.DEFAULT_STACKTRACE_PATTERN + this.vals[4] + SyslogAppender.DEFAULT_STACKTRACE_PATTERN + this.vals[5] + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + this.vals[6] + SyslogAppender.DEFAULT_STACKTRACE_PATTERN + this.vals[7] + SyslogAppender.DEFAULT_STACKTRACE_PATTERN + this.vals[8];
    }
}
