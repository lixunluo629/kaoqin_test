package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/MinFunction.class */
public class MinFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        if (numParam == 0) {
            return Double.MIN_VALUE;
        }
        double min = Double.MAX_VALUE;
        for (int i = 0; i < numParam; i++) {
            if (d[i] < min) {
                min = d[i];
            }
        }
        return min;
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam >= 0;
    }

    public String toString() {
        return "min(x1, x2, ..., xn)";
    }
}
