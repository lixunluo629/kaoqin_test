package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/NaturalCubicSpline.class */
public class NaturalCubicSpline extends ParametricCurve {
    private static double[][] pt = new double[0];
    private static double[][] data = new double[0];
    private static int ci = 0;
    private boolean closed;

    public NaturalCubicSpline(ControlPath cp, GroupIterator gi) {
        super(cp, gi);
        this.closed = false;
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    protected void eval(double[] p) {
        int n = p.length - 1;
        double t = p[n];
        double t2 = t * t;
        double t3 = t2 * t;
        int j = 0;
        for (int i = 0; i < n; i++) {
            int i2 = j;
            int j2 = j + 1;
            int j3 = j2 + 1;
            double d = data[i2][ci] + (data[j2][ci] * t);
            int j4 = j3 + 1;
            double d2 = d + (data[j3][ci] * t2);
            j = j4 + 1;
            p[i] = d2 + (data[j4][ci] * t3);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v44 */
    private static void precalc(int n, int dim, boolean closed) {
        int n2 = n - 1;
        double[] a = data[4 * dim];
        double[] b = data[(4 * dim) + 1];
        double[] c = data[(4 * dim) + 2];
        int k = 0;
        if (closed) {
            ?? r0 = data[(4 * dim) + 3];
            for (int j = 0; j < dim; j++) {
                a[1] = 0.25d;
                r0[r0] = 4598175219545276416;
                b[0] = 0.25d * 3.0d * (pt[1][j] - pt[n2][j]);
                double h = 4.0d;
                double f = 3.0d * (pt[0][j] - pt[n2 - 1][j]);
                double g = 1.0d;
                for (int i = 1; i < n2; i++) {
                    double e = 1.0d / (4.0d - a[i]);
                    a[i + 1] = e;
                    r0[i + 1] = (-e) * r0[i];
                    b[i] = e * ((3.0d * (pt[i + 1][j] - pt[i - 1][j])) - b[i - 1]);
                    h -= g * r0[i];
                    f -= g * b[i - 1];
                    g = (-a[i]) * g;
                }
                double h2 = h - ((g + 1.0d) * (a[n2] + r0[n2]));
                b[n2] = f - ((g + 1.0d) * b[n2 - 1]);
                c[n2] = b[n2] / h2;
                c[n2 - 1] = b[n2 - 1] - ((a[n2] + r0[n2]) * c[n2]);
                for (int i2 = n2 - 2; i2 >= 0; i2--) {
                    c[i2] = (b[i2] - (a[i2 + 1] * c[i2 + 1])) - (r0[i2 + 1] * c[n2]);
                }
                int i3 = k;
                int k2 = k + 1;
                double[] w = data[i3];
                int k3 = k2 + 1;
                double[] x = data[k2];
                int k4 = k3 + 1;
                double[] y = data[k3];
                k = k4 + 1;
                double[] z = data[k4];
                for (int i4 = 0; i4 < n2; i4++) {
                    w[i4] = pt[i4][j];
                    x[i4] = c[i4];
                    y[i4] = ((3.0d * (pt[i4 + 1][j] - pt[i4][j])) - (2.0d * c[i4])) - c[i4 + 1];
                    z[i4] = (2.0d * (pt[i4][j] - pt[i4 + 1][j])) + c[i4] + c[i4 + 1];
                }
                w[n2] = pt[n2][j];
                x[n2] = c[n2];
                y[n2] = ((3.0d * (pt[0][j] - pt[n2][j])) - (2.0d * c[n2])) - c[0];
                z[n2] = (2.0d * (pt[n2][j] - pt[0][j])) + c[n2] + c[0];
            }
            return;
        }
        for (int j2 = 0; j2 < dim; j2++) {
            a[0] = 0.5d;
            for (int i5 = 1; i5 < n2; i5++) {
                a[i5] = 1.0d / (4.0d - a[i5 - 1]);
            }
            a[n2] = 1.0d / (2.0d - a[n2 - 1]);
            b[0] = a[0] * 3.0d * (pt[1][j2] - pt[0][j2]);
            for (int i6 = 1; i6 < n2; i6++) {
                b[i6] = a[i6] * ((3.0d * (pt[i6 + 1][j2] - pt[i6 - 1][j2])) - b[i6 - 1]);
            }
            b[n2] = a[n2] * ((3.0d * (pt[n2][j2] - pt[n2 - 1][j2])) - b[n2 - 1]);
            c[n2] = b[n2];
            for (int i7 = n2 - 1; i7 >= 0; i7--) {
                c[i7] = b[i7] - (a[i7] * c[i7 + 1]);
            }
            int i8 = k;
            int k5 = k + 1;
            double[] w2 = data[i8];
            int k6 = k5 + 1;
            double[] x2 = data[k5];
            int k7 = k6 + 1;
            double[] y2 = data[k6];
            k = k7 + 1;
            double[] z2 = data[k7];
            for (int i9 = 0; i9 < n2; i9++) {
                w2[i9] = pt[i9][j2];
                x2[i9] = c[i9];
                y2[i9] = ((3.0d * (pt[i9 + 1][j2] - pt[i9][j2])) - (2.0d * c[i9])) - c[i9 + 1];
                z2[i9] = (2.0d * (pt[i9][j2] - pt[i9 + 1][j2])) + c[i9] + c[i9 + 1];
            }
            w2[n2] = pt[n2][j2];
            x2[n2] = 0.0d;
            y2[n2] = 0.0d;
            z2[n2] = 0.0d;
        }
    }

    public void setClosed(boolean b) {
        this.closed = b;
    }

    public boolean getClosed() {
        return this.closed;
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    public int getSampleLimit() {
        return 1;
    }

    /* JADX WARN: Type inference failed for: r0v38, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r0v43, types: [double[], double[][]] */
    @Override // com.graphbuilder.curve.Curve
    public void appendTo(MultiPath mp) {
        if (!this.gi.isInRange(0, this.cp.numPoints())) {
            throw new IllegalArgumentException("Group iterator not in range");
        }
        int n = this.gi.getGroupSize();
        if (n < 2) {
            throw new IllegalArgumentException("Group iterator size < 2");
        }
        int dim = mp.getDimension();
        int x = 3 + (4 * dim) + 1;
        if (data.length < x) {
            ?? r0 = new double[x];
            for (int i = 0; i < data.length; i++) {
                r0[i] = data[i];
            }
            data = r0;
        }
        if (pt.length < n) {
            int m = 2 * n;
            pt = new double[m];
            for (int i2 = 0; i2 < data.length; i2++) {
                data[i2] = new double[m];
            }
        }
        this.gi.set(0, 0);
        for (int i3 = 0; i3 < n; i3++) {
            pt[i3] = this.cp.getPoint(this.gi.next()).getLocation();
        }
        precalc(n, dim, this.closed);
        ci = 0;
        double[] p = new double[dim + 1];
        eval(p);
        if (this.connect) {
            mp.lineTo(p);
        } else {
            mp.moveTo(p);
        }
        for (int i4 = 0; i4 < n; i4++) {
            ci = i4;
            BinaryCurveApproximationAlgorithm.genPts(this, 0.0d, 1.0d, mp);
        }
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r0v7, types: [double[], double[][]] */
    @Override // com.graphbuilder.curve.Curve
    public void resetMemory() {
        if (pt.length > 0) {
            pt = new double[0];
        }
        if (data.length > 0) {
            data = new double[0];
        }
    }
}
