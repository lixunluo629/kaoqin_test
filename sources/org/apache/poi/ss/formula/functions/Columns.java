package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Columns.class */
public final class Columns extends Fixed1ArgFunction {
    @Override // org.apache.poi.ss.formula.functions.Function1Arg
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        int result;
        if (arg0 instanceof TwoDEval) {
            result = ((TwoDEval) arg0).getWidth();
        } else if (arg0 instanceof RefEval) {
            result = 1;
        } else {
            return ErrorEval.VALUE_INVALID;
        }
        return new NumberEval(result);
    }
}
