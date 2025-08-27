package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/BSpline.class */
public class BSpline extends ParametricCurve {
    public static final int UNIFORM_CLAMPED = 0;
    public static final int UNIFORM_UNCLAMPED = 1;
    public static final int NON_UNIFORM = 2;
    private static int[] a = new int[0];
    private static int[] c = new int[0];
    private static double[] knot = new double[0];
    private ValueVector knotVector;
    private double t_min;
    private double t_max;
    private int sampleLimit;
    private int degree;
    private int knotVectorType;
    private boolean useDefaultInterval;

    public BSpline(ControlPath cp, GroupIterator gi) {
        super(cp, gi);
        this.knotVector = new ValueVector(new double[]{0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 1.0d, 1.0d, 1.0d}, 8);
        this.t_min = 0.0d;
        this.t_max = 1.0d;
        this.sampleLimit = 1;
        this.degree = 4;
        this.knotVectorType = 0;
        this.useDefaultInterval = true;
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    protected void eval(double[] p) {
        int dim = p.length - 1;
        double t = p[dim];
        int numPts = this.gi.getGroupSize();
        this.gi.set(0, 0);
        for (int i = 0; i < numPts; i++) {
            double w = N(t, i);
            double[] loc = this.cp.getPoint(this.gi.next()).getLocation();
            for (int j = 0; j < dim; j++) {
                int i2 = j;
                p[i2] = p[i2] + (loc[j] * w);
            }
        }
    }

    public void setInterval(double t_min, double t_max) {
        if (t_min > t_max) {
            throw new IllegalArgumentException("t_min <= t_max required.");
        }
        this.t_min = t_min;
        this.t_max = t_max;
    }

    public double t_min() {
        return this.t_min;
    }

    public double t_max() {
        return this.t_max;
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    public int getSampleLimit() {
        return this.sampleLimit;
    }

    public void setSampleLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Sample-limit >= 0 required.");
        }
        this.sampleLimit = limit;
    }

    public int getDegree() {
        return this.degree - 1;
    }

    public void setDegree(int d) {
        if (d <= 0) {
            throw new IllegalArgumentException("Degree > 0 required.");
        }
        this.degree = d + 1;
    }

    public ValueVector getKnotVector() {
        return this.knotVector;
    }

    public void setKnotVector(ValueVector v) {
        if (v == null) {
            throw new IllegalArgumentException("Knot-vector cannot be null.");
        }
        this.knotVector = v;
    }

    public boolean getUseDefaultInterval() {
        return this.useDefaultInterval;
    }

    public void setUseDefaultInterval(boolean b) {
        this.useDefaultInterval = b;
    }

    public int getKnotVectorType() {
        return this.knotVectorType;
    }

    public void setKnotVectorType(int type) {
        if (type < 0 || type > 2) {
            throw new IllegalArgumentException("Unknown knot-vector type.");
        }
        this.knotVectorType = type;
    }

    @Override // com.graphbuilder.curve.Curve
    public void appendTo(MultiPath mp) {
        if (!this.gi.isInRange(0, this.cp.numPoints())) {
            throw new IllegalArgumentException("Group iterator not in range");
        }
        int numPts = this.gi.getGroupSize();
        int f = numPts - this.degree;
        if (f < 0) {
            throw new IllegalArgumentException("group iterator size - degree < 0");
        }
        int x = numPts + this.degree;
        if (knot.length < x) {
            knot = new double[2 * x];
        }
        double t1 = this.t_min;
        double t2 = this.t_max;
        if (this.knotVectorType == 2) {
            if (this.knotVector.size() != x) {
                throw new IllegalArgumentException("knotVector.size(" + this.knotVector.size() + ") != " + x);
            }
            knot[0] = this.knotVector.get(0);
            for (int i = 1; i < x; i++) {
                knot[i] = this.knotVector.get(i);
                if (knot[i] < knot[i - 1]) {
                    throw new IllegalArgumentException("Knot not in sorted order! (knot[" + i + "] < knot[" + i + "-1])");
                }
            }
        } else if (this.knotVectorType == 1) {
            double grad = 1.0d / (x - 1);
            for (int i2 = 0; i2 < x; i2++) {
                knot[i2] = i2 * grad;
            }
            if (this.useDefaultInterval) {
                t1 = (this.degree - 1) * grad;
                t2 = 1.0d - ((this.degree - 1) * grad);
            }
        } else if (this.knotVectorType == 0) {
            double grad2 = 1.0d / (f + 1);
            for (int i3 = 0; i3 < this.degree; i3++) {
                knot[i3] = 0.0d;
            }
            int j = this.degree;
            for (int i4 = 1; i4 <= f; i4++) {
                int i5 = j;
                j++;
                knot[i5] = i4 * grad2;
            }
            for (int i6 = j; i6 < x; i6++) {
                knot[i6] = 1.0d;
            }
            if (this.useDefaultInterval) {
                t1 = 0.0d;
                t2 = 1.0d;
            }
        }
        if (a.length < this.degree) {
            a = new int[2 * this.degree];
            c = new int[2 * this.degree];
        }
        double[] p = new double[mp.getDimension() + 1];
        p[mp.getDimension()] = t1;
        eval(p);
        if (this.connect) {
            mp.lineTo(p);
        } else {
            mp.moveTo(p);
        }
        BinaryCurveApproximationAlgorithm.genPts(this, t1, t2, mp);
    }

    protected double N(double t, int i) {
        boolean reset;
        double d = 0.0d;
        int j = 0;
        while (true) {
            if (j >= this.degree) {
                break;
            }
            double t1 = knot[i + j];
            double t2 = knot[i + j + 1];
            if (t < t1 || t > t2 || t1 == t2) {
                j++;
            } else {
                int dm2 = this.degree - 2;
                for (int k = (this.degree - j) - 1; k >= 0; k--) {
                    a[k] = 0;
                }
                if (j > 0) {
                    for (int k2 = 0; k2 < j; k2++) {
                        c[k2] = k2;
                    }
                    c[j] = Integer.MAX_VALUE;
                } else {
                    c[0] = dm2;
                    c[1] = this.degree;
                }
                int z = 0;
                while (true) {
                    if (c[z] < c[z + 1] - 1) {
                        double e = 1.0d;
                        int bc = 0;
                        int y = dm2 - j;
                        int p = j - 1;
                        int m = dm2;
                        int n = this.degree;
                        while (m >= 0) {
                            if (p >= 0 && c[p] == m) {
                                int w = i + bc;
                                double kd = knot[w + n];
                                e *= (kd - t) / (kd - knot[w + 1]);
                                bc++;
                                p--;
                            } else {
                                int w2 = i + a[y];
                                double kw = knot[w2];
                                e *= (t - kw) / (knot[(w2 + n) - 1] - kw);
                                y--;
                            }
                            m--;
                            n--;
                        }
                        if (j > 0) {
                            int g = 0;
                            boolean z2 = false;
                            while (true) {
                                reset = z2;
                                int[] iArr = a;
                                int i2 = g;
                                iArr[i2] = iArr[i2] + 1;
                                if (a[g] <= j) {
                                    break;
                                }
                                g++;
                                z2 = true;
                            }
                            if (reset) {
                                for (int h = g - 1; h >= 0; h--) {
                                    a[h] = a[g];
                                }
                            }
                        }
                        d += e;
                        int[] iArr2 = c;
                        int i3 = z;
                        iArr2[i3] = iArr2[i3] + 1;
                        if (c[z] > dm2) {
                            break;
                        }
                        for (int k3 = 0; k3 < z; k3++) {
                            c[k3] = k3;
                        }
                        z = 0;
                    } else {
                        z++;
                    }
                }
            }
        }
        return d;
    }

    @Override // com.graphbuilder.curve.Curve
    public void resetMemory() {
        if (a.length > 0) {
            a = new int[0];
            c = new int[0];
        }
        if (knot.length > 0) {
            knot = new double[0];
        }
    }
}
