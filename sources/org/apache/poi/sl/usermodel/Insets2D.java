package org.apache.poi.sl.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/Insets2D.class */
public final class Insets2D implements Cloneable {
    public double top;
    public double left;
    public double bottom;
    public double right;

    public Insets2D(double top, double left, double bottom, double right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public void set(double top, double left, double bottom, double right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Insets2D) {
            Insets2D insets = (Insets2D) obj;
            return this.top == insets.top && this.left == insets.left && this.bottom == insets.bottom && this.right == insets.right;
        }
        return false;
    }

    public int hashCode() {
        double sum1 = this.left + this.bottom;
        double sum2 = this.right + this.top;
        double val1 = ((sum1 * (sum1 + 1.0d)) / 2.0d) + this.left;
        double val2 = ((sum2 * (sum2 + 1.0d)) / 2.0d) + this.top;
        double sum3 = val1 + val2;
        return (int) (((sum3 * (sum3 + 1.0d)) / 2.0d) + val2);
    }

    public String toString() {
        return getClass().getName() + "[top=" + this.top + ",left=" + this.left + ",bottom=" + this.bottom + ",right=" + this.right + "]";
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Insets2D m3600clone() {
        return new Insets2D(this.top, this.left, this.bottom, this.right);
    }
}
