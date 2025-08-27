package org.apache.poi.ss.formula.atp;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/atp/RandBetween.class */
final class RandBetween implements FreeRefFunction {
    public static final FreeRefFunction instance = new RandBetween();

    private RandBetween() {
    }

    @Override // org.apache.poi.ss.formula.functions.FreeRefFunction
    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length != 2) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            double bottom = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[0], ec.getRowIndex(), ec.getColumnIndex()));
            double top = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[1], ec.getRowIndex(), ec.getColumnIndex()));
            if (bottom > top) {
                return ErrorEval.NUM_ERROR;
            }
            double bottom2 = Math.ceil(bottom);
            double top2 = Math.floor(top);
            if (bottom2 > top2) {
                top2 = bottom2;
            }
            return new NumberEval(bottom2 + ((int) (Math.random() * ((top2 - bottom2) + 1.0d))));
        } catch (EvaluationException e) {
            return ErrorEval.VALUE_INVALID;
        }
    }
}
