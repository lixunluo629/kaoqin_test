package com.graphbuilder.math.func;

import com.graphbuilder.math.PascalsTriangle;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/func/CombinFunction.class */
public class CombinFunction implements Function {
    @Override // com.graphbuilder.math.func.Function
    public double of(double[] d, int numParam) {
        int n = (int) d[0];
        int r = (int) d[1];
        return PascalsTriangle.nCr(n, r);
    }

    @Override // com.graphbuilder.math.func.Function
    public boolean acceptNumParam(int numParam) {
        return numParam == 2;
    }

    public String toString() {
        return "combin(n, r)";
    }
}
