package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/FreeRefFunction.class */
public interface FreeRefFunction {
    ValueEval evaluate(ValueEval[] valueEvalArr, OperationEvaluationContext operationEvaluationContext);
}
