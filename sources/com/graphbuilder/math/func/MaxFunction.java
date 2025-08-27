package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/MaxFunction.class */
public class MaxFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        if (numParam == 0) {
            return Double.MAX_VALUE;
        }
        double max = -1.7976931348623157E308d;
        for (int i = 0; i < numParam; i++) {
            if (d[i] > max) {
                max = d[i];
            }
        }
        return max;
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam >= 0;
    }

    public String toString() {
        return "max(x1, x2, ..., xn)";
    }
}
