package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/AvgFunction.class */
public class AvgFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        double sum = 0.0d;
        for (int i = 0; i < numParam; i++) {
            sum += d[i];
        }
        return sum / numParam;
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam > 0;
    }

    public String toString() {
        return "avg(x1, x2, ..., xn)";
    }
}
