package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.ThreeDEval;
import org.apache.poi.ss.formula.TwoDEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/eval/AreaEval.class */
public interface AreaEval extends TwoDEval, ThreeDEval {
    int getFirstRow();

    int getLastRow();

    int getFirstColumn();

    int getLastColumn();

    ValueEval getAbsoluteValue(int i, int i2);

    boolean contains(int i, int i2);

    boolean containsColumn(int i);

    boolean containsRow(int i);

    @Override // org.apache.poi.ss.formula.TwoDEval
    int getWidth();

    @Override // org.apache.poi.ss.formula.TwoDEval
    int getHeight();

    ValueEval getRelativeValue(int i, int i2);

    AreaEval offset(int i, int i2, int i3, int i4);
}
