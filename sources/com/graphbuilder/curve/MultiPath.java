package com.graphbuilder.curve;

import com.graphbuilder.geom.Geom;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/MultiPath.class */
public class MultiPath {
    public static final Object MOVE_TO = new Object();
    public static final Object LINE_TO = new Object();
    private double[][] point = new double[2][0];
    private Object[] type = new Object[this.point.length];
    private int size = 0;
    private double flatness = 1.0d;
    private final int dimension;

    public MultiPath(int dimension) {
        if (dimension <= 0) {
            throw new IllegalArgumentException("dimension > 0 required");
        }
        this.dimension = dimension;
    }

    public int getDimension() {
        return this.dimension;
    }

    public double getFlatness() {
        return this.flatness;
    }

    public void setFlatness(double f) {
        if (f <= 0.0d) {
            throw new IllegalArgumentException("flatness > 0 required");
        }
        this.flatness = f;
    }

    public double[] get(int index) {
        return this.point[index];
    }

    public void set(int index, double[] p) {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }
        if (p.length < this.dimension) {
            throw new IllegalArgumentException("p.length >= dimension required");
        }
        if (this.point[index] == null) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        this.point[index] = p;
    }

    public Object getType(int index) {
        if (this.type[index] == null) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return this.type[index];
    }

    public void setType(int index, Object type) {
        if (type != MOVE_TO && type != LINE_TO) {
            throw new IllegalArgumentException("unknown type");
        }
        if (this.type[index] == null) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        if (index == 0 && type != MOVE_TO) {
            throw new IllegalArgumentException("type[0] must always be MOVE_TO");
        }
        this.type[index] = type;
    }

    public int getNumPoints() {
        return this.size;
    }

    public void setNumPoints(int n) {
        if (n != 0 && this.point[n - 1] == null) {
            throw new ArrayIndexOutOfBoundsException(n);
        }
        this.size = n;
    }

    public int getCapacity() {
        return this.point.length;
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [double[], double[][]] */
    public void ensureCapacity(int capacity) {
        if (this.point.length < capacity) {
            int x = 2 * this.point.length;
            if (x < capacity) {
                x = capacity;
            }
            ?? r0 = new double[x];
            for (int i = 0; i < this.size; i++) {
                r0[i] = this.point[i];
            }
            Object[] t2 = new Object[x];
            for (int i2 = 0; i2 < this.size; i2++) {
                t2[i2] = this.type[i2];
            }
            this.point = r0;
            this.type = t2;
        }
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [double[], double[][]] */
    public void trimArray() {
        if (this.size < this.point.length) {
            ?? r0 = new double[this.size];
            for (int i = 0; i < this.size; i++) {
                r0[i] = this.point[i];
            }
            Object[] t2 = new Object[this.size];
            for (int i2 = 0; i2 < this.size; i2++) {
                t2[i2] = this.type[i2];
            }
            this.point = r0;
            this.type = t2;
        }
    }

    public void lineTo(double[] p) {
        append(p, LINE_TO);
    }

    public void moveTo(double[] p) {
        append(p, MOVE_TO);
    }

    private void append(double[] p, Object t) {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }
        if (p.length < this.dimension) {
            throw new IllegalArgumentException("p.length >= dimension required");
        }
        if (this.size == 0) {
            t = MOVE_TO;
        }
        ensureCapacity(this.size + 1);
        this.point[this.size] = p;
        this.type[this.size] = t;
        this.size++;
    }

    public double getDistSq(double[] p) {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }
        if (p.length < this.dimension) {
            throw new IllegalArgumentException("p.length >= dimension required");
        }
        int n = getNumPoints();
        if (n == 0) {
            return Double.MAX_VALUE;
        }
        double dist = Double.MAX_VALUE;
        double[] b = get(0);
        double[] c = new double[this.dimension + 1];
        for (int i = 1; i < n; i++) {
            double[] a = get(i);
            if (getType(i) == LINE_TO) {
                double d = Geom.ptSegDistSq(a, b, p, c, this.dimension);
                if (d < dist) {
                    dist = d;
                }
            }
        }
        return dist;
    }
}
