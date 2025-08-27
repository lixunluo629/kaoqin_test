package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.LinearRegressionFunction;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Intercept.class */
public final class Intercept extends Fixed2ArgFunction {
    private final LinearRegressionFunction func = new LinearRegressionFunction(LinearRegressionFunction.FUNCTION.INTERCEPT);

    @Override // org.apache.poi.ss.formula.functions.Function2Arg
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        return this.func.evaluate(srcRowIndex, srcColumnIndex, arg0, arg1);
    }
}
