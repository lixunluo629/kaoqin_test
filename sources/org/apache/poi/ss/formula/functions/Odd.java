package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.functions.NumericFunction;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Odd.class */
public final class Odd extends NumericFunction.OneArg {
    private static final long PARITY_MASK = -2;

    @Override // org.apache.poi.ss.formula.functions.NumericFunction.OneArg
    protected double evaluate(double d) {
        if (d == 0.0d) {
            return 1.0d;
        }
        return d > 0.0d ? calcOdd(d) : -calcOdd(-d);
    }

    private static long calcOdd(double d) {
        double dpm1 = d + 1.0d;
        long x = ((long) dpm1) & (-2);
        return Double.compare((double) x, dpm1) == 0 ? x - 1 : x + 1;
    }
}
