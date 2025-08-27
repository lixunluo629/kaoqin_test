package com.graphbuilder.curve;

import com.graphbuilder.math.PascalsTriangle;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/BezierCurve.class */
public class BezierCurve extends ParametricCurve {
    private static double[] a = new double[0];
    private double t_min;
    private double t_max;
    private int sampleLimit;

    public BezierCurve(ControlPath cp, GroupIterator gi) {
        super(cp, gi);
        this.t_min = 0.0d;
        this.t_max = 1.0d;
        this.sampleLimit = 1;
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    public void eval(double[] p) {
        double t = p[p.length - 1];
        int numPts = this.gi.getGroupSize();
        if (numPts > a.length) {
            a = new double[2 * numPts];
        }
        a[numPts - 1] = 1.0d;
        double b = 1.0d;
        double one_minus_t = 1.0d - t;
        for (int i = numPts - 2; i >= 0; i--) {
            a[i] = a[i + 1] * one_minus_t;
        }
        this.gi.set(0, 0);
        for (int i2 = 0; i2 < numPts; i2++) {
            double pt = PascalsTriangle.nCr(numPts - 1, i2);
            if (!Double.isInfinite(pt) && !Double.isNaN(pt)) {
                double gravity = a[i2] * b * pt;
                double[] d = this.cp.getPoint(this.gi.next()).getLocation();
                for (int j = 0; j < p.length - 1; j++) {
                    p[j] = p[j] + (d[j] * gravity);
                }
            }
            b *= t;
        }
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

    @Override // com.graphbuilder.curve.Curve
    public void appendTo(MultiPath mp) {
        if (!this.gi.isInRange(0, this.cp.numPoints())) {
            throw new IllegalArgumentException("group iterator not in range");
        }
        int n = mp.getDimension();
        double[] d = new double[n + 1];
        d[n] = this.t_min;
        eval(d);
        if (this.connect) {
            mp.lineTo(d);
        } else {
            mp.moveTo(d);
        }
        BinaryCurveApproximationAlgorithm.genPts(this, this.t_min, this.t_max, mp);
    }

    @Override // com.graphbuilder.curve.Curve
    public void resetMemory() {
        if (a.length > 0) {
            a = new double[0];
        }
    }
}
