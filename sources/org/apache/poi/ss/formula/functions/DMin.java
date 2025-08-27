package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.NumericValueEval;
import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/DMin.class */
public final class DMin implements IDStarAlgorithm {
    private ValueEval minimumValue;

    @Override // org.apache.poi.ss.formula.functions.IDStarAlgorithm
    public boolean processMatch(ValueEval eval) {
        if (eval instanceof NumericValueEval) {
            if (this.minimumValue == null) {
                this.minimumValue = eval;
                return true;
            }
            double currentValue = ((NumericValueEval) eval).getNumberValue();
            double oldValue = ((NumericValueEval) this.minimumValue).getNumberValue();
            if (currentValue < oldValue) {
                this.minimumValue = eval;
                return true;
            }
            return true;
        }
        return true;
    }

    @Override // org.apache.poi.ss.formula.functions.IDStarAlgorithm
    public ValueEval getResult() {
        if (this.minimumValue == null) {
            return NumberEval.ZERO;
        }
        return this.minimumValue;
    }
}
