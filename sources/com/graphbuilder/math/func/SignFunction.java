package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/SignFunction.class */
public class SignFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        if (d[0] > 0.0d) {
            return 1.0d;
        }
        return d[0] < 0.0d ? -1.0d : 0.0d;
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam == 1;
    }

    public String toString() {
        return "sign(x)";
    }
}
