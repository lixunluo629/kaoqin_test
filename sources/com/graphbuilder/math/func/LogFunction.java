package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/LogFunction.class */
public class LogFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        if (numParam == 1) {
            return Math.log(d[0]) / Math.log(10.0d);
        }
        return Math.log(d[0]) / Math.log(d[1]);
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam == 1 || numParam == 2;
    }

    public String toString() {
        return "log(x):log(x, y)";
    }
}
