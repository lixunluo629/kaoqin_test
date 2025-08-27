package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/Function2Arg.class */
public interface Function2Arg extends Function {
    ValueEval evaluate(int i, int i2, ValueEval valueEval, ValueEval valueEval2);
}
