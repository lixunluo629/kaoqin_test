package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/CubicBSpline.class */
public class CubicBSpline extends ParametricCurve {
    private static int section = 0;
    private static int numPoints = 0;
    private static double[][] pt = new double[4];
    private static double[] b = new double[4];
    private boolean interpolateEndpoints;

    public CubicBSpline(ControlPath cp, GroupIterator gi) {
        super(cp, gi);
        this.interpolateEndpoints = false;
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    protected void eval(double[] p) {
        double t = p[p.length - 1];
        double t2 = t * t;
        double t3 = t2 * t;
        double u = 1.0d - t;
        double u2 = u * u;
        double u3 = u2 * u;
        if (numPoints == 4) {
            b[0] = u2 * u;
            b[1] = 3.0d * u2 * t;
            b[2] = 3.0d * u * t2;
            b[3] = t3;
        } else if (numPoints == 5) {
            if (section == 0) {
                b[0] = u3;
                b[1] = (((7.0d * t3) / 4.0d) - ((9.0d * t2) / 2.0d)) + (3.0d * t);
                b[2] = (-t3) + ((3.0d * t2) / 2.0d);
                b[3] = t3 / 4.0d;
            } else {
                b[0] = u3 / 4.0d;
                b[1] = (-u3) + ((3.0d * u2) / 2.0d);
                b[2] = (((7.0d * u3) / 4.0d) - ((9.0d * u2) / 2.0d)) + (3.0d * u);
                b[3] = t3;
            }
        } else if (numPoints == 6) {
            if (section == 0) {
                b[0] = u3;
                b[1] = (((7.0d * t3) / 4.0d) - ((9.0d * t2) / 2.0d)) + (3.0d * t);
                b[2] = (((-11.0d) * t3) / 12.0d) + ((3.0d * t2) / 2.0d);
                b[3] = t3 / 6.0d;
            } else if (section == 1) {
                b[0] = u3 / 4.0d;
                b[1] = (((7.0d * t3) / 12.0d) - ((5.0d * t2) / 4.0d)) + (t / 4.0d) + 0.5833333333333334d;
                b[2] = (((-7.0d) * t3) / 12.0d) + (t2 / 2.0d) + (t / 2.0d) + 0.16666666666666666d;
                b[3] = t3 / 4.0d;
            } else {
                b[0] = u3 / 6.0d;
                b[1] = (((-11.0d) * u3) / 12.0d) + ((3.0d * u2) / 2.0d);
                b[2] = (((7.0d * u3) / 4.0d) - ((9.0d * u2) / 2.0d)) + (3.0d * u);
                b[3] = t3;
            }
        } else if (section == 0) {
            b[0] = u3;
            b[1] = (((7.0d * t3) / 4.0d) - ((9.0d * t2) / 2.0d)) + (3.0d * t);
            b[2] = (((-11.0d) * t3) / 12.0d) + ((3.0d * t2) / 2.0d);
            b[3] = t3 / 6.0d;
        } else if (section == 1) {
            b[0] = u3 / 4.0d;
            b[1] = (((7.0d * t3) / 12.0d) - ((5.0d * t2) / 4.0d)) + (t / 4.0d) + 0.5833333333333334d;
            b[2] = ((-t3) / 2.0d) + (t2 / 2.0d) + (t / 2.0d) + 0.16666666666666666d;
            b[3] = t3 / 6.0d;
        } else if (section == 2) {
            b[0] = u3 / 6.0d;
            b[1] = ((t3 / 2.0d) - t2) + 0.6666666666666666d;
            b[2] = ((((-t3) + t2) + t) / 2.0d) + 0.16666666666666666d;
            b[3] = t3 / 6.0d;
        } else if (section == 3) {
            b[0] = u3 / 6.0d;
            b[1] = ((-u3) / 2.0d) + (u2 / 2.0d) + (u / 2.0d) + 0.16666666666666666d;
            b[2] = (((7.0d * u3) / 12.0d) - ((5.0d * u2) / 4.0d)) + (u / 4.0d) + 0.5833333333333334d;
            b[3] = t3 / 4.0d;
        } else {
            b[0] = u3 / 6.0d;
            b[1] = (((-11.0d) * u3) / 12.0d) + ((3.0d * u2) / 2.0d);
            b[2] = (((7.0d * u3) / 4.0d) - ((9.0d * u2) / 2.0d)) + (3.0d * u);
            b[3] = t3;
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < p.length - 1; j++) {
                p[j] = p[j] + (pt[i][j] * b[i]);
            }
        }
    }

    @Override // com.graphbuilder.curve.ParametricCurve
    public int getSampleLimit() {
        return 1;
    }

    public void setInterpolateEndpoints(boolean b2) {
        this.interpolateEndpoints = b2;
    }

    public boolean getInterpolateEndpoints() {
        return this.interpolateEndpoints;
    }

    @Override // com.graphbuilder.curve.Curve
    public void appendTo(MultiPath mp) {
        if (!this.gi.isInRange(0, this.cp.numPoints())) {
            throw new IllegalArgumentException("Group iterator not in range");
        }
        int n = this.gi.getGroupSize();
        if (n < 4) {
            throw new IllegalArgumentException("Group iterator size < 4");
        }
        if (this.interpolateEndpoints) {
            numPoints = n;
            section = 0;
        } else {
            numPoints = -1;
            section = 2;
        }
        this.gi.set(0, 0);
        int index_i = 0;
        int count_j = 0;
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
        int j = 3;
        while (true) {
            BinaryCurveApproximationAlgorithm.genPts(this, 0.0d, 1.0d, mp);
            j++;
            if (j != n) {
                this.gi.set(index_i, count_j);
                this.gi.next();
                index_i = this.gi.index_i();
                count_j = this.gi.count_j();
                for (int i2 = 0; i2 < 4; i2++) {
                    pt[i2] = this.cp.getPoint(this.gi.next()).getLocation();
                }
                if (this.interpolateEndpoints) {
                    if (n < 7) {
                        section++;
                    } else {
                        if (section != 2) {
                            section++;
                        }
                        if (section == 2 && j == n - 2) {
                            section++;
                        }
                    }
                }
            } else {
                return;
            }
        }
    }
}
