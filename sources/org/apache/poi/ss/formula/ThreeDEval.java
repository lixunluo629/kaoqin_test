package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ThreeDEval.class */
public interface ThreeDEval extends TwoDEval, SheetRange {
    ValueEval getValue(int i, int i2, int i3);
}
