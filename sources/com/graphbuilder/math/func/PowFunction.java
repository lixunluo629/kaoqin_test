package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/PowFunction.class */
public class PowFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        return Math.pow(d[0], d[1]);
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam == 2;
    }

    public String toString() {
        return "pow(x, y)";
    }
}
