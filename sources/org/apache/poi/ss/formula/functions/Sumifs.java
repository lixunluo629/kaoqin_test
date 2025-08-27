package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Sumifs.class */
public final class Sumifs extends Baseifs {
    public static final FreeRefFunction instance = new Sumifs();

    @Override // org.apache.poi.ss.formula.functions.Baseifs, org.apache.poi.ss.formula.functions.FreeRefFunction
    public /* bridge */ /* synthetic */ ValueEval evaluate(ValueEval[] x0, OperationEvaluationContext x1) {
        return super.evaluate(x0, x1);
    }

    @Override // org.apache.poi.ss.formula.functions.Baseifs
    protected boolean hasInitialRange() {
        return true;
    }
}
