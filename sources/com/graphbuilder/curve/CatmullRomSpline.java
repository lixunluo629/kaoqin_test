package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/CatmullRomSpline.class */
public class CatmullRomSpline extends ParametricCurve {
    private static double[][] pt = new double[4];

    public CatmullRomSpline(ControlPath cp, GroupIterator gi) {
        super(cp, gi);
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    protected void eval(double[] p) {
        double t = p[p.length - 1];
        double t2 = t * t;
        double t3 = t2 * t;
        for (int i = 0; i < p.length - 1; i++) {
            p[i] = (0.5d * ((((pt[3][i] - pt[0][i]) + (3.0d * (pt[1][i] - pt[2][i]))) * t3) + ((((2.0d * (pt[0][i] + (2.0d * pt[2][i]))) - (5.0d * pt[1][i])) - pt[3][i]) * t2) + ((pt[2][i] - pt[0][i]) * t))) + pt[1][i];
        }
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    public int getSampleLimit() {
        return 1;
    }

    @Override // com.graphbuilder.curve.Curve
    public void appendTo(MultiPath mp) {
        if (!this.gi.isInRange(0, this.cp.numPoints())) {
            throw new IllegalArgumentException("Group iterator not in range");
        }
        if (this.gi.getGroupSize() < 4) {
            throw new IllegalArgumentException("Group iterator size < 4");
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
