package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/RoundFunction.class */
public class RoundFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        if (d[0] >= 9.223372036854776E18d || d[0] <= -9.223372036854776E18d) {
            return d[0];
        }
        return Math.round(d[0]);
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam == 1;
    }

    public String toString() {
        return "round(x)";
    }
}
