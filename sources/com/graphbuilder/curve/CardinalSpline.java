package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/CardinalSpline.class */
public class CardinalSpline extends ParametricCurve {
    private static double[][] pt = new double[4];
    private double alpha;

    public CardinalSpline(ControlPath cp, GroupIterator gi) {
        super(cp, gi);
        this.alpha = 0.5d;
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    protected void eval(double[] p) {
        double t = p[p.length - 1];
        double t2 = t * t;
        double t3 = t2 * t;
        double a = ((2.0d * t3) - (3.0d * t2)) + 1.0d;
        double b = ((-2.0d) * t3) + (3.0d * t2);
        double c = this.alpha * ((t3 - (2.0d * t2)) + t);
        double d = this.alpha * (t3 - t2);
        for (int i = 0; i < p.length - 1; i++) {
            p[i] = (a * pt[1][i]) + (b * pt[2][i]) + (c * (pt[2][i] - pt[0][i])) + (d * (pt[3][i] - pt[1][i]));
        }
    }

    public double getAlpha() {
        return this.alpha;
    }

    public void setAlpha(double a) {
        this.alpha = a;
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    public int getSampleLimit() {
        return 1;
    }

    @Override // com.graphbuilder.curve.Curve
    public void appendTo(MultiPath mp) {
        if (!this.gi.isInRange(0, this.cp.numPoints())) {
            throw new IllegalArgumentException("group iterator not in range");
        }
        if (this.gi.getGroupSize() < 4) {
            throw new IllegalArgumentException("more than 4 groups required");
        }
        this.gi.set(0, 0);
        for (int i = 0; i < 4; i++) {
            pt[i] = this.cp.getPoint(this.gi.next()).getLocation();
        }
        double[] d = new double[mp.getDimension() + 1];
        eval(d);
        if (this.connect) {
            mp.lineTo(d);
        } else {
            mp.moveTo(d);
        }
        this.gi.set(0, 0);
        while (true) {
            int index_i = this.gi.index_i();
            int count_j = this.gi.count_j();
            for (int i2 = 0; i2 < 4; i2++) {
                if (!this.gi.hasNext()) {
                    throw new IllegalArgumentException("Group iterator ended early");
                }
                pt[i2] = this.cp.getPoint(this.gi.next()).getLocation();
            }
            this.gi.set(index_i, count_j);
            this.gi.next();
            BinaryCurveApproximationAlgorithm.genPts(this, 0.0d, 1.0d, mp);
        }
    }
}
