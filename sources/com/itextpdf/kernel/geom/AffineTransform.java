package com.itextpdf.kernel.geom;

import java.io.Serializable;
import java.util.Objects;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/geom/AffineTransform.class */
public class AffineTransform implements Cloneable, Serializable {
    private static final long serialVersionUID = 1330973210523860834L;
    public static final int TYPE_IDENTITY = 0;
    public static final int TYPE_TRANSLATION = 1;
    public static final int TYPE_UNIFORM_SCALE = 2;
    public static final int TYPE_GENERAL_SCALE = 4;
    public static final int TYPE_QUADRANT_ROTATION = 8;
    public static final int TYPE_GENERAL_ROTATION = 16;
    public static final int TYPE_GENERAL_TRANSFORM = 32;
    public static final int TYPE_FLIP = 64;
    public static final int TYPE_MASK_SCALE = 6;
    public static final int TYPE_MASK_ROTATION = 24;
    static final int TYPE_UNKNOWN = -1;
    static final double ZERO = 1.0E-10d;
    double m00;
    double m10;
    double m01;
    double m11;
    double m02;
    double m12;
    int type;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.itextpdf.kernel.geom.AffineTransform] */
    public AffineTransform() {
        this.type = 0;
        this.m11 = 1.0d;
        this.m00 = 1.0d;
        ?? r4 = 0;
        this.m12 = 0.0d;
        this.m02 = 0.0d;
        r4.m01 = this;
        this.m10 = this;
    }

    public AffineTransform(AffineTransform t) {
        this.type = t.type;
        this.m00 = t.m00;
        this.m10 = t.m10;
        this.m01 = t.m01;
        this.m11 = t.m11;
        this.m02 = t.m02;
        this.m12 = t.m12;
    }

    public AffineTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        this.type = -1;
        this.m00 = m00;
        this.m10 = m10;
        this.m01 = m01;
        this.m11 = m11;
        this.m02 = m02;
        this.m12 = m12;
    }

    public AffineTransform(float[] matrix) {
        this.type = -1;
        this.m00 = matrix[0];
        this.m10 = matrix[1];
        this.m01 = matrix[2];
        this.m11 = matrix[3];
        if (matrix.length > 4) {
            this.m02 = matrix[4];
            this.m12 = matrix[5];
        }
    }

    public AffineTransform(double[] matrix) {
        this.type = -1;
        this.m00 = matrix[0];
        this.m10 = matrix[1];
        this.m01 = matrix[2];
        this.m11 = matrix[3];
        if (matrix.length > 4) {
            this.m02 = matrix[4];
            this.m12 = matrix[5];
        }
    }

    public int getType() {
        if (this.type != -1) {
            return this.type;
        }
        int type = 0;
        if ((this.m00 * this.m01) + (this.m10 * this.m11) != 0.0d) {
            return 0 | 32;
        }
        if (this.m02 != 0.0d || this.m12 != 0.0d) {
            type = 0 | 1;
        } else if (this.m00 == 1.0d && this.m11 == 1.0d && this.m01 == 0.0d && this.m10 == 0.0d) {
            return 0;
        }
        if ((this.m00 * this.m11) - (this.m01 * this.m10) < 0.0d) {
            type |= 64;
        }
        double dx = (this.m00 * this.m00) + (this.m10 * this.m10);
        double dy = (this.m01 * this.m01) + (this.m11 * this.m11);
        if (dx != dy) {
            type |= 4;
        } else if (dx != 1.0d) {
            type |= 2;
        }
        if ((this.m00 == 0.0d && this.m11 == 0.0d) || (this.m10 == 0.0d && this.m01 == 0.0d && (this.m00 < 0.0d || this.m11 < 0.0d))) {
            type |= 8;
        } else if (this.m01 != 0.0d || this.m10 != 0.0d) {
            type |= 16;
        }
        return type;
    }

    public double getScaleX() {
        return this.m00;
    }

    public double getScaleY() {
        return this.m11;
    }

    public double getShearX() {
        return this.m01;
    }

    public double getShearY() {
        return this.m10;
    }

    public double getTranslateX() {
        return this.m02;
    }

    public double getTranslateY() {
        return this.m12;
    }

    public boolean isIdentity() {
        return getType() == 0;
    }

    public void getMatrix(float[] matrix) {
        matrix[0] = (float) this.m00;
        matrix[1] = (float) this.m10;
        matrix[2] = (float) this.m01;
        matrix[3] = (float) this.m11;
        if (matrix.length > 4) {
            matrix[4] = (float) this.m02;
            matrix[5] = (float) this.m12;
        }
    }

    public void getMatrix(double[] matrix) {
        matrix[0] = this.m00;
        matrix[1] = this.m10;
        matrix[2] = this.m01;
        matrix[3] = this.m11;
        if (matrix.length > 4) {
            matrix[4] = this.m02;
            matrix[5] = this.m12;
        }
    }

    public double getDeterminant() {
        return (this.m00 * this.m11) - (this.m01 * this.m10);
    }

    public void setTransform(float m00, float m10, float m01, float m11, float m02, float m12) {
        this.type = -1;
        this.m00 = m00;
        this.m10 = m10;
        this.m01 = m01;
        this.m11 = m11;
        this.m02 = m02;
        this.m12 = m12;
    }

    public void setTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        this.type = -1;
        this.m00 = m00;
        this.m10 = m10;
        this.m01 = m01;
        this.m11 = m11;
        this.m02 = m02;
        this.m12 = m12;
    }

    public void setTransform(AffineTransform t) {
        this.type = t.type;
        setTransform(t.m00, t.m10, t.m01, t.m11, t.m02, t.m12);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.itextpdf.kernel.geom.AffineTransform] */
    public void setToIdentity() {
        this.type = 0;
        this.m11 = 1.0d;
        this.m00 = 1.0d;
        ?? r4 = 0;
        this.m12 = 0.0d;
        this.m02 = 0.0d;
        r4.m01 = this;
        this.m10 = this;
    }

    public void setToTranslation(double mx, double my) {
        this.m11 = 1.0d;
        this.m00 = 1.0d;
        this.m10 = 0.0d;
        this.m01 = 0.0d;
        this.m02 = mx;
        this.m12 = my;
        if (mx == 0.0d && my == 0.0d) {
            this.type = 0;
        } else {
            this.type = 1;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.itextpdf.kernel.geom.AffineTransform] */
    public void setToScale(double scx, double scy) {
        this.m00 = scx;
        this.m11 = scy;
        ?? r4 = 0;
        this.m12 = 0.0d;
        this.m02 = 0.0d;
        r4.m01 = this;
        this.m10 = this;
        if (scx != 1.0d || scy != 1.0d) {
            this.type = -1;
        } else {
            this.type = 0;
        }
    }

    public void setToShear(double shx, double shy) {
        this.m11 = 1.0d;
        this.m00 = 1.0d;
        this.m12 = 0.0d;
        this.m02 = 0.0d;
        this.m01 = shx;
        this.m10 = shy;
        if (shx != 0.0d || shy != 0.0d) {
            this.type = -1;
        } else {
            this.type = 0;
        }
    }

    public void setToRotation(double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        if (Math.abs(cos) < ZERO) {
            cos = 0.0d;
            sin = sin > 0.0d ? 1.0d : -1.0d;
        } else if (Math.abs(sin) < ZERO) {
            sin = 0.0d;
            cos = cos > 0.0d ? 1.0d : -1.0d;
        }
        double d = (float) cos;
        this.m11 = d;
        this.m00 = d;
        this.m01 = (float) (-sin);
        this.m10 = (float) sin;
        this.m12 = 0.0d;
        this.m02 = 0.0d;
        this.type = -1;
    }

    public void setToRotation(double angle, double px, double py) {
        setToRotation(angle);
        this.m02 = (px * (1.0d - this.m00)) + (py * this.m10);
        this.m12 = (py * (1.0d - this.m00)) - (px * this.m10);
        this.type = -1;
    }

    public static AffineTransform getTranslateInstance(double mx, double my) {
        AffineTransform t = new AffineTransform();
        t.setToTranslation(mx, my);
        return t;
    }

    public static AffineTransform getScaleInstance(double scx, double scY) {
        AffineTransform t = new AffineTransform();
        t.setToScale(scx, scY);
        return t;
    }

    public static AffineTransform getShearInstance(double shx, double shy) {
        AffineTransform m = new AffineTransform();
        m.setToShear(shx, shy);
        return m;
    }

    public static AffineTransform getRotateInstance(double angle) {
        AffineTransform t = new AffineTransform();
        t.setToRotation(angle);
        return t;
    }

    public static AffineTransform getRotateInstance(double angle, double x, double y) {
        AffineTransform t = new AffineTransform();
        t.setToRotation(angle, x, y);
        return t;
    }

    public void translate(double mx, double my) {
        concatenate(getTranslateInstance(mx, my));
    }

    public void scale(double scx, double scy) {
        concatenate(getScaleInstance(scx, scy));
    }

    public void shear(double shx, double shy) {
        concatenate(getShearInstance(shx, shy));
    }

    public void rotate(double angle) {
        concatenate(getRotateInstance(angle));
    }

    public void rotate(double angle, double px, double py) {
        concatenate(getRotateInstance(angle, px, py));
    }

    AffineTransform multiply(AffineTransform t1, AffineTransform t2) {
        return new AffineTransform((t1.m00 * t2.m00) + (t1.m10 * t2.m01), (t1.m00 * t2.m10) + (t1.m10 * t2.m11), (t1.m01 * t2.m00) + (t1.m11 * t2.m01), (t1.m01 * t2.m10) + (t1.m11 * t2.m11), (t1.m02 * t2.m00) + (t1.m12 * t2.m01) + t2.m02, (t1.m02 * t2.m10) + (t1.m12 * t2.m11) + t2.m12);
    }

    public void concatenate(AffineTransform t) {
        setTransform(multiply(t, this));
    }

    public void preConcatenate(AffineTransform t) {
        setTransform(multiply(this, t));
    }

    public AffineTransform createInverse() throws NoninvertibleTransformException {
        double det = getDeterminant();
        if (Math.abs(det) < ZERO) {
            throw new NoninvertibleTransformException(NoninvertibleTransformException.DETERMINANT_IS_ZERO_CANNOT_INVERT_TRANSFORMATION);
        }
        return new AffineTransform(this.m11 / det, (-this.m10) / det, (-this.m01) / det, this.m00 / det, ((this.m01 * this.m12) - (this.m11 * this.m02)) / det, ((this.m10 * this.m02) - (this.m00 * this.m12)) / det);
    }

    public Point transform(Point src, Point dst) {
        if (dst == null) {
            dst = new Point();
        }
        double x = src.getX();
        double y = src.getY();
        dst.setLocation((x * this.m00) + (y * this.m01) + this.m02, (x * this.m10) + (y * this.m11) + this.m12);
        return dst;
    }

    public void transform(Point[] src, int srcOff, Point[] dst, int dstOff, int length) {
        while (true) {
            length--;
            if (length >= 0) {
                int i = srcOff;
                srcOff++;
                Point srcPoint = src[i];
                double x = srcPoint.getX();
                double y = srcPoint.getY();
                Point dstPoint = dst[dstOff];
                if (dstPoint == null) {
                    dstPoint = new Point();
                }
                dstPoint.setLocation((x * this.m00) + (y * this.m01) + this.m02, (x * this.m10) + (y * this.m11) + this.m12);
                int i2 = dstOff;
                dstOff++;
                dst[i2] = dstPoint;
            } else {
                return;
            }
        }
    }

    public void transform(double[] src, int srcOff, double[] dst, int dstOff, int length) {
        int step = 2;
        if (src == dst && srcOff < dstOff && dstOff < srcOff + (length * 2)) {
            srcOff = (srcOff + (length * 2)) - 2;
            dstOff = (dstOff + (length * 2)) - 2;
            step = -2;
        }
        while (true) {
            length--;
            if (length >= 0) {
                double x = src[srcOff + 0];
                double y = src[srcOff + 1];
                dst[dstOff + 0] = (x * this.m00) + (y * this.m01) + this.m02;
                dst[dstOff + 1] = (x * this.m10) + (y * this.m11) + this.m12;
                srcOff += step;
                dstOff += step;
            } else {
                return;
            }
        }
    }

    public void transform(float[] src, int srcOff, float[] dst, int dstOff, int length) {
        int step = 2;
        if (src == dst && srcOff < dstOff && dstOff < srcOff + (length * 2)) {
            srcOff = (srcOff + (length * 2)) - 2;
            dstOff = (dstOff + (length * 2)) - 2;
            step = -2;
        }
        while (true) {
            length--;
            if (length >= 0) {
                float x = src[srcOff + 0];
                float y = src[srcOff + 1];
                dst[dstOff + 0] = (float) ((x * this.m00) + (y * this.m01) + this.m02);
                dst[dstOff + 1] = (float) ((x * this.m10) + (y * this.m11) + this.m12);
                srcOff += step;
                dstOff += step;
            } else {
                return;
            }
        }
    }

    public void transform(float[] src, int srcOff, double[] dst, int dstOff, int length) {
        while (true) {
            length--;
            if (length >= 0) {
                int i = srcOff;
                int srcOff2 = srcOff + 1;
                float x = src[i];
                srcOff = srcOff2 + 1;
                float y = src[srcOff2];
                int i2 = dstOff;
                int dstOff2 = dstOff + 1;
                dst[i2] = (x * this.m00) + (y * this.m01) + this.m02;
                dstOff = dstOff2 + 1;
                dst[dstOff2] = (x * this.m10) + (y * this.m11) + this.m12;
            } else {
                return;
            }
        }
    }

    public void transform(double[] src, int srcOff, float[] dst, int dstOff, int length) {
        while (true) {
            length--;
            if (length >= 0) {
                int i = srcOff;
                int srcOff2 = srcOff + 1;
                double x = src[i];
                srcOff = srcOff2 + 1;
                double y = src[srcOff2];
                int i2 = dstOff;
                int dstOff2 = dstOff + 1;
                dst[i2] = (float) ((x * this.m00) + (y * this.m01) + this.m02);
                dstOff = dstOff2 + 1;
                dst[dstOff2] = (float) ((x * this.m10) + (y * this.m11) + this.m12);
            } else {
                return;
            }
        }
    }

    public Point deltaTransform(Point src, Point dst) {
        if (dst == null) {
            dst = new Point();
        }
        double x = src.getX();
        double y = src.getY();
        dst.setLocation((x * this.m00) + (y * this.m01), (x * this.m10) + (y * this.m11));
        return dst;
    }

    public void deltaTransform(double[] src, int srcOff, double[] dst, int dstOff, int length) {
        while (true) {
            length--;
            if (length >= 0) {
                int i = srcOff;
                int srcOff2 = srcOff + 1;
                double x = src[i];
                srcOff = srcOff2 + 1;
                double y = src[srcOff2];
                int i2 = dstOff;
                int dstOff2 = dstOff + 1;
                dst[i2] = (x * this.m00) + (y * this.m01);
                dstOff = dstOff2 + 1;
                dst[dstOff2] = (x * this.m10) + (y * this.m11);
            } else {
                return;
            }
        }
    }

    public Point inverseTransform(Point src, Point dst) throws NoninvertibleTransformException {
        double det = getDeterminant();
        if (Math.abs(det) < ZERO) {
            throw new NoninvertibleTransformException(NoninvertibleTransformException.DETERMINANT_IS_ZERO_CANNOT_INVERT_TRANSFORMATION);
        }
        if (dst == null) {
            dst = new Point();
        }
        double x = src.getX() - this.m02;
        double y = src.getY() - this.m12;
        dst.setLocation(((x * this.m11) - (y * this.m01)) / det, ((y * this.m00) - (x * this.m10)) / det);
        return dst;
    }

    public void inverseTransform(double[] src, int srcOff, double[] dst, int dstOff, int length) throws NoninvertibleTransformException {
        double det = getDeterminant();
        if (Math.abs(det) < ZERO) {
            throw new NoninvertibleTransformException(NoninvertibleTransformException.DETERMINANT_IS_ZERO_CANNOT_INVERT_TRANSFORMATION);
        }
        while (true) {
            length--;
            if (length >= 0) {
                int i = srcOff;
                int srcOff2 = srcOff + 1;
                double x = src[i] - this.m02;
                srcOff = srcOff2 + 1;
                double y = src[srcOff2] - this.m12;
                int i2 = dstOff;
                int dstOff2 = dstOff + 1;
                dst[i2] = ((x * this.m11) - (y * this.m01)) / det;
                dstOff = dstOff2 + 1;
                dst[dstOff2] = ((y * this.m00) - (x * this.m10)) / det;
            } else {
                return;
            }
        }
    }

    public void inverseTransform(float[] src, int srcOff, float[] dst, int dstOff, int length) throws NoninvertibleTransformException {
        float det = (float) getDeterminant();
        if (Math.abs(det) < ZERO) {
            throw new NoninvertibleTransformException(NoninvertibleTransformException.DETERMINANT_IS_ZERO_CANNOT_INVERT_TRANSFORMATION);
        }
        while (true) {
            length--;
            if (length >= 0) {
                int i = srcOff;
                float x = (float) (src[i] - this.m02);
                srcOff = srcOff + 1 + 1;
                float y = (float) (src[r11] - this.m12);
                int i2 = dstOff;
                int dstOff2 = dstOff + 1;
                dst[i2] = (float) (((x * this.m11) - (y * this.m01)) / det);
                dstOff = dstOff2 + 1;
                dst[dstOff2] = (float) (((y * this.m00) - (x * this.m10)) / det);
            } else {
                return;
            }
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public AffineTransform m822clone() throws CloneNotSupportedException {
        return (AffineTransform) super.clone();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AffineTransform that = (AffineTransform) o;
        return Double.compare(that.m00, this.m00) == 0 && Double.compare(that.m10, this.m10) == 0 && Double.compare(that.m01, this.m01) == 0 && Double.compare(that.m11, this.m11) == 0 && Double.compare(that.m02, this.m02) == 0 && Double.compare(that.m12, this.m12) == 0;
    }

    public int hashCode() {
        return Objects.hash(Double.valueOf(this.m00), Double.valueOf(this.m10), Double.valueOf(this.m01), Double.valueOf(this.m11), Double.valueOf(this.m02), Double.valueOf(this.m12));
    }
}
