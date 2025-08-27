package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.CountUtils;
import org.apache.poi.ss.formula.functions.Countif;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Baseifs.class */
abstract class Baseifs implements FreeRefFunction {
    protected abstract boolean hasInitialRange();

    Baseifs() {
    }

    @Override // org.apache.poi.ss.formula.functions.FreeRefFunction
    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        boolean hasInitialRange = hasInitialRange();
        int firstCriteria = hasInitialRange ? 1 : 0;
        if (args.length < 2 + firstCriteria || args.length % 2 != firstCriteria) {
            return ErrorEval.VALUE_INVALID;
        }
        AreaEval sumRange = null;
        if (hasInitialRange) {
            try {
                sumRange = convertRangeArg(args[0]);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }
        AreaEval[] ae = new AreaEval[(args.length - firstCriteria) / 2];
        CountUtils.I_MatchPredicate[] mp = new CountUtils.I_MatchPredicate[ae.length];
        int i = firstCriteria;
        int k = 0;
        while (i < args.length) {
            ae[k] = convertRangeArg(args[i]);
            mp[k] = Countif.createCriteriaPredicate(args[i + 1], ec.getRowIndex(), ec.getColumnIndex());
            i += 2;
            k++;
        }
        validateCriteriaRanges(sumRange, ae);
        validateCriteria(mp);
        double result = aggregateMatchingCells(sumRange, ae, mp);
        return new NumberEval(result);
    }

    private static void validateCriteriaRanges(AreaEval sumRange, AreaEval[] criteriaRanges) throws EvaluationException {
        int h = criteriaRanges[0].getHeight();
        int w = criteriaRanges[0].getWidth();
        if (sumRange != null && (sumRange.getHeight() != h || sumRange.getWidth() != w)) {
            throw EvaluationException.invalidValue();
        }
        for (AreaEval r : criteriaRanges) {
            if (r.getHeight() != h || r.getWidth() != w) {
                throw EvaluationException.invalidValue();
            }
        }
    }

    private static void validateCriteria(CountUtils.I_MatchPredicate[] criteria) throws EvaluationException {
        for (CountUtils.I_MatchPredicate predicate : criteria) {
            if (predicate instanceof Countif.ErrorMatcher) {
                throw new EvaluationException(ErrorEval.valueOf(((Countif.ErrorMatcher) predicate).getValue()));
            }
        }
    }

    private static double aggregateMatchingCells(AreaEval sumRange, AreaEval[] ranges, CountUtils.I_MatchPredicate[] predicates) {
        int height = ranges[0].getHeight();
        int width = ranges[0].getWidth();
        double result = 0.0d;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                boolean matches = true;
                for (int i = 0; i < ranges.length; i++) {
                    AreaEval aeRange = ranges[i];
                    CountUtils.I_MatchPredicate mp = predicates[i];
                    if (mp == null || !mp.matches(aeRange.getRelativeValue(r, c))) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    result += accumulate(sumRange, r, c);
                }
            }
        }
        return result;
    }

    private static double accumulate(AreaEval sumRange, int relRowIndex, int relColIndex) {
        if (sumRange == null) {
            return 1.0d;
        }
        ValueEval addend = sumRange.getRelativeValue(relRowIndex, relColIndex);
        if (addend instanceof NumberEval) {
            return ((NumberEval) addend).getNumberValue();
        }
        return 0.0d;
    }

    protected static AreaEval convertRangeArg(ValueEval eval) throws EvaluationException {
        if (eval instanceof AreaEval) {
            return (AreaEval) eval;
        }
        if (eval instanceof RefEval) {
            return ((RefEval) eval).offset(0, 0, 0, 0);
        }
        throw new EvaluationException(ErrorEval.VALUE_INVALID);
    }
}
