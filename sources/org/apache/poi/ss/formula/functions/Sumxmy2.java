package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.functions.XYNumericFunction;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Sumxmy2.class */
public final class Sumxmy2 extends XYNumericFunction {
    private static final XYNumericFunction.Accumulator XMinusYSquaredAccumulator = new XYNumericFunction.Accumulator() { // from class: org.apache.poi.ss.formula.functions.Sumxmy2.1
        @Override // org.apache.poi.ss.formula.functions.XYNumericFunction.Accumulator
        public double accumulate(double x, double y) {
            double xmy = x - y;
            return xmy * xmy;
        }
    };

    @Override // org.apache.poi.ss.formula.functions.XYNumericFunction
    protected XYNumericFunction.Accumulator createAccumulator() {
        return XMinusYSquaredAccumulator;
    }
}
