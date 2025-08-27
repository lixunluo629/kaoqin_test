package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.functions.XYNumericFunction;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Sumx2my2.class */
public final class Sumx2my2 extends XYNumericFunction {
    private static final XYNumericFunction.Accumulator XSquaredMinusYSquaredAccumulator = new XYNumericFunction.Accumulator() { // from class: org.apache.poi.ss.formula.functions.Sumx2my2.1
        @Override // org.apache.poi.ss.formula.functions.XYNumericFunction.Accumulator
        public double accumulate(double x, double y) {
            return (x * x) - (y * y);
        }
    };

    @Override // org.apache.poi.ss.formula.functions.XYNumericFunction
    protected XYNumericFunction.Accumulator createAccumulator() {
        return XSquaredMinusYSquaredAccumulator;
    }
}
