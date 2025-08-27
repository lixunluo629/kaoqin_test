package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.functions.NumericFunction;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Even.class */
public final class Even extends NumericFunction.OneArg {
    private static final long PARITY_MASK = -2;

    @Override // org.apache.poi.ss.formula.functions.NumericFunction.OneArg
    protected double evaluate(double d) {
        long result;
        if (d == 0.0d) {
            return 0.0d;
        }
        if (d > 0.0d) {
            result = calcEven(d);
        } else {
            result = -calcEven(-d);
        }
        return result;
    }

    private static long calcEven(double d) {
        long x = ((long) d) & (-2);
        if (x == d) {
            return x;
        }
        return x + 2;
    }
}
