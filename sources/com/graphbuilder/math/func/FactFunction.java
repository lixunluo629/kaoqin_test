package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/FactFunction.class */
public class FactFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        int n = (int) d[0];
        double result = 1.0d;
        for (int i = n; i > 1; i--) {
            result *= i;
        }
        return result;
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam == 1;
    }

    public String toString() {
        return "fact(n)";
    }
}
