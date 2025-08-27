package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/TanhFunction.class */
public class TanhFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        double e = Math.pow(2.718281828459045d, 2.0d * d[0]);
        return (e - 1.0d) / (e + 1.0d);
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam == 1;
    }

    public String toString() {
        return "tanh(x)";
    }
}
