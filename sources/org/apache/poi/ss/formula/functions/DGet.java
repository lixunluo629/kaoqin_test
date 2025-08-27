package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/DGet.class */
public final class DGet implements IDStarAlgorithm {
    private ValueEval result;

    @Override // org.apache.poi.ss.formula.functions.IDStarAlgorithm
    public boolean processMatch(ValueEval eval) {
        if (this.result == null) {
            this.result = eval;
            return true;
        }
        if (this.result instanceof BlankEval) {
            this.result = eval;
            return true;
        }
        if (!(eval instanceof BlankEval)) {
            this.result = ErrorEval.NUM_ERROR;
            return false;
        }
        return true;
    }

    @Override // org.apache.poi.ss.formula.functions.IDStarAlgorithm
    public ValueEval getResult() {
        if (this.result == null) {
            return ErrorEval.VALUE_INVALID;
        }
        if (this.result instanceof BlankEval) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            if (OperandResolver.coerceValueToString(OperandResolver.getSingleValue(this.result, 0, 0)).equals("")) {
                return ErrorEval.VALUE_INVALID;
            }
            return this.result;
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }
}
