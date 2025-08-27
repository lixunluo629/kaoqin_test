package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Var1or2ArgFunction.class */
abstract class Var1or2ArgFunction implements Function1Arg, Function2Arg {
    Var1or2ArgFunction() {
    }

    @Override // org.apache.poi.ss.formula.functions.Function
    public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        switch (args.length) {
            case 1:
                return evaluate(srcRowIndex, srcColumnIndex, args[0]);
            case 2:
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1]);
            default:
                return ErrorEval.VALUE_INVALID;
        }
    }
}
