package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/IDStarAlgorithm.class */
public interface IDStarAlgorithm {
    boolean processMatch(ValueEval valueEval);

    ValueEval getResult();
}
