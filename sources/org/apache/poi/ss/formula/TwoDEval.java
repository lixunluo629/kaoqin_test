package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/TwoDEval.class */
public interface TwoDEval extends ValueEval {
    ValueEval getValue(int i, int i2);

    int getWidth();

    int getHeight();

    boolean isRow();

    boolean isColumn();

    TwoDEval getRow(int i);

    TwoDEval getColumn(int i);

    boolean isSubTotal(int i, int i2);
}
