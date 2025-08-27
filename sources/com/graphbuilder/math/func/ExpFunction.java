package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/ExpFunction.class */
public class ExpFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        return Math.exp(d[0]);
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam == 1;
    }

    public String toString() {
        return "exp(x)";
    }
}
