package org.apache.poi.ss.formula;

import org.apache.poi.util.Internal;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationSheet.class */
public interface EvaluationSheet {
    EvaluationCell getCell(int i, int i2);

    void clearAllCachedResultValues();
}
