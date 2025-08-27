package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Function.class */
public interface Function {
    ValueEval evaluate(ValueEval[] valueEvalArr, int i, int i2);
}
