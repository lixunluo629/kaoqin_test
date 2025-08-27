package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/NURBSpline.class */
public class NURBSpline extends BSpline {
    private static double[] nw = new double[0];
    private static double[] weight = new double[0];
    private ValueVector weightVector;
    private boolean useWeightVector;

    public NURBSpline(ControlPath cp, GroupIterator gi) {
        super(cp, gi);
        this.weightVector = new ValueVector(new double[]{1.0d, 1.0d, 1.0d, 1.0d}, 4);
        this.useWeightVector = true;
    }

    @Override // com.graphbuilder.curve.BSpline, com.graphbuilder.curve.ParametricCurve
    protected void eval(double[] p) {
        int dim = p.length - 1;
        double t = p[dim];
        double sum2 = 0.0d;
        int numPts = this.gi.getGroupSize();
        for (int i = 0; i < numPts; i++) {
            nw[i] = N(t, i) * weight[i];
            sum2 += nw[i];
        }
        if (sum2 == 0.0d) {
            sum2 = 1.0d;
        }
        for (int i2 = 0; i2 < dim; i2++) {
            double sum1 = 0.0d;
            this.gi.set(0, 0);
            for (int j = 0; j < numPts; j++) {
                sum1 += nw[j] * this.cp.getPoint(this.gi.next()).getLocation()[i2];
            }
            p[i2] = sum1 / sum2;
        }
    }

    public ValueVector getWeightVector() {
        return this.weightVector;
    }

    public void setWeightVector(ValueVector v) {
        if (v == null) {
            throw new IllegalArgumentException("Weight-vector cannot be null.");
        }
        this.weightVector = v;
    }

    public boolean getUseWeightVector() {
        return this.useWeightVector;
    }

    public void setUseWeightVector(boolean b) {
        this.useWeightVector = b;
    }

    @Override // com.graphbuilder.curve.BSpline, com.graphbuilder.curve.Curve
    public void appendTo(MultiPath mp) {
        if (!this.gi.isInRange(0, this.cp.numPoints())) {
            throw new IllegalArgumentException("Group iterator not in range");
        }
        int numPts = this.gi.getGroupSize();
        if (nw.length < numPts) {
            nw = new double[2 * numPts];
            weight = new double[2 * numPts];
        }
        if (this.useWeightVector) {
            if (this.weightVector.size() != numPts) {
                throw new IllegalArgumentException("weightVector.size(" + this.weightVector.size() + ") != group iterator size(" + numPts + ")");
            }
            for (int i = 0; i < numPts; i++) {
                weight[i] = this.weightVector.get(i);
                if (weight[i] < 0.0d) {
                    throw new IllegalArgumentException("Negative weight not allowed");
                }
            }
        } else {
            for (int i2 = 0; i2 < numPts; i2++) {
                weight[i2] = 1.0d;
            }
        }
        super.appendTo(mp);
    }

    @Override // com.graphbuilder.curve.BSpline, com.graphbuilder.curve.Curve
    public void resetMemory() {
        super.resetMemory();
        if (nw.length > 0) {
            nw = new double[0];
            weight = new double[0];
        }
    }
}
