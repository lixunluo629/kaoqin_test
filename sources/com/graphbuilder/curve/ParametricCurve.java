package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/ParametricCurve.class */
public abstract class ParametricCurve extends Curve {
    protected abstract void eval(double[] dArr);

    public abstract int getSampleLimit();

    public ParametricCurve(ControlPath cp, GroupIterator gp) {
        super(cp, gp);
    }
}
