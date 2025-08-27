package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.SheetRange;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/eval/RefEval.class */
public interface RefEval extends ValueEval, SheetRange {
    ValueEval getInnerValueEval(int i);

    int getColumn();

    int getRow();

    @Override // org.apache.poi.ss.formula.SheetRange
    int getFirstSheetIndex();

    @Override // org.apache.poi.ss.formula.SheetRange
    int getLastSheetIndex();

    int getNumberOfSheets();

    AreaEval offset(int i, int i2, int i3, int i4);
}
