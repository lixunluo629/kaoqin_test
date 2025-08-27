package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/LagrangeCurve.class */
public class LagrangeCurve extends ParametricCurve {
    private ValueVector knotVector;
    private int baseIndex;
    private int baseLength;
    private boolean interpolateFirst;
    private boolean interpolateLast;
    private static double[][] pt = new double[0];

    public LagrangeCurve(ControlPath cp, GroupIterator gi) {
        super(cp, gi);
        this.knotVector = new ValueVector(new double[]{0.0d, 0.3333333333333333d, 0.6666666666666666d, 1.0d}, 4);
        this.baseIndex = 1;
        this.baseLength = 1;
        this.interpolateFirst = false;
        this.interpolateLast = false;
    }

    public int getBaseIndex() {
        return this.baseIndex;
    }

    public void setBaseIndex(int b) {
        if (b < 0) {
            throw new IllegalArgumentException("base index >= 0 required.");
        }
        this.baseIndex = b;
    }

    public int getBaseLength() {
        return this.baseLength;
    }

    public void setBaseLength(int b) {
        if (b <= 0) {
            throw new IllegalArgumentException("base length > 0 required.");
        }
        this.baseLength = b;
    }

    public boolean getInterpolateFirst() {
        return this.interpolateFirst;
    }

    public boolean getInterpolateLast() {
        return this.interpolateLast;
    }

    public void setInterpolateFirst(boolean b) {
        this.interpolateFirst = b;
    }

    public void setInterpolateLast(boolean b) {
        this.interpolateLast = b;
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

    @Override // com.graphbuilder.curve.ParametricCurve
    public int getSampleLimit() {
        return 1;
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    protected void eval(double[] p) {
        double t = p[p.length - 1];
        int n = this.knotVector.size();
        for (int i = 0; i < n; i++) {
            double[] q = pt[i];
            double L = L(t, i);
            for (int j = 0; j < p.length - 1; j++) {
                int i2 = j;
                p[i2] = p[i2] + (q[j] * L);
            }
        }
    }

    private double L(double t, int i) {
        double d = 1.0d;
        int n = this.knotVector.size();
        for (int j = 0; j < n; j++) {
            double e = this.knotVector.get(i) - this.knotVector.get(j);
            if (e != 0.0d) {
                d *= (t - this.knotVector.get(j)) / e;
            }
        }
        return d;
    }

    /* JADX WARN: Type inference failed for: r0v75, types: [double[], double[][]] */
    @Override // com.graphbuilder.curve.Curve
    public void appendTo(MultiPath mp) {
        if (!this.gi.isInRange(0, this.cp.numPoints())) {
            throw new IllegalArgumentException("Group iterator not in range");
        }
        if (this.baseIndex + this.baseLength >= this.knotVector.size()) {
            throw new IllegalArgumentException("baseIndex + baseLength >= knotVector.size");
        }
        if (pt.length < this.knotVector.size()) {
            pt = new double[2 * this.knotVector.size()];
        }
        this.gi.set(0, 0);
        boolean b = false;
        if (this.baseIndex != 0 && this.interpolateFirst) {
            for (int i = 0; i < this.knotVector.size(); i++) {
                if (!this.gi.hasNext()) {
                    throw new IllegalArgumentException("Group iterator ended early");
                }
                pt[i] = this.cp.getPoint(this.gi.next()).getLocation();
            }
            b = doBCAA(mp, this.knotVector.get(0), this.knotVector.get(this.baseIndex), false);
        }
        this.gi.set(0, 0);
        int last_i = 0;
        int last_j = 0;
        while (true) {
            int temp_i = this.gi.index_i();
            int temp_j = this.gi.count_j();
            int index_i = 0;
            int count_j = 0;
            int i2 = 0;
            int j = 0;
            while (j < this.knotVector.size()) {
                if (i2 == this.baseLength) {
                    index_i = this.gi.index_i();
                    count_j = this.gi.count_j();
                }
                if (!this.gi.hasNext()) {
                    break;
                }
                pt[j] = this.cp.getPoint(this.gi.next()).getLocation();
                i2++;
                j++;
            }
            if (j < this.knotVector.size()) {
                break;
            }
            this.gi.set(index_i, count_j);
            last_i = temp_i;
            last_j = temp_j;
            b = doBCAA(mp, this.knotVector.get(this.baseIndex), this.knotVector.get(this.baseIndex + this.baseLength), b);
        }
        if (this.baseIndex + this.baseLength < this.knotVector.size() - 1 && this.interpolateLast) {
            this.gi.set(last_i, last_j);
            for (int i3 = 0; i3 < this.knotVector.size(); i3++) {
                if (!this.gi.hasNext()) {
                    System.out.println("not enough points to interpolate last");
                    return;
                }
                pt[i3] = this.cp.getPoint(this.gi.next()).getLocation();
            }
            doBCAA(mp, this.knotVector.get(this.baseIndex + this.baseLength), this.knotVector.get(this.knotVector.size() - 1), b);
        }
    }

    private boolean doBCAA(MultiPath mp, double t1, double t2, boolean b) {
        if (t2 < t1) {
            t1 = t2;
            t2 = t1;
        }
        if (!b) {
            b = true;
            double[] d = new double[mp.getDimension() + 1];
            d[mp.getDimension()] = t1;
            eval(d);
            if (this.connect) {
                mp.lineTo(d);
            } else {
                mp.moveTo(d);
            }
        }
        BinaryCurveApproximationAlgorithm.genPts(this, t1, t2, mp);
        return b;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [double[], double[][]] */
    @Override // com.graphbuilder.curve.Curve
    public void resetMemory() {
        if (pt.length > 0) {
            pt = new double[0];
        }
    }
}
