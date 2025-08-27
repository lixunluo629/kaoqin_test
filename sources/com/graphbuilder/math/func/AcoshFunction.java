package com.graphbuilder.math.func;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/AcoshFunction.class */
public class AcoshFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        double a = Math.sqrt((d[0] + 1.0d) / 2.0d);
        double b = Math.sqrt((d[0] - 1.0d) / 2.0d);
        return 2.0d * Math.log(a + b);
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam == 1;
    }

    public String toString() {
        return "acosh(x)";
    }
}
