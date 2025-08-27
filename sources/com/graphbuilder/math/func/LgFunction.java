package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/LgFunction.class */
public class LgFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        return Math.log(d[0]) / Math.log(2.0d);
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam == 1;
    }

    public String toString() {
        return "lg(x)";
    }
}
