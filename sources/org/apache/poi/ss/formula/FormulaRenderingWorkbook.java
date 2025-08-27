package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.EvaluationWorkbook;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/FormulaRenderingWorkbook.class */
public interface FormulaRenderingWorkbook {
    EvaluationWorkbook.ExternalSheet getExternalSheet(int i);

    String getSheetFirstNameByExternSheet(int i);

    String getSheetLastNameByExternSheet(int i);

    String resolveNameXText(NameXPtg nameXPtg);

    String getNameText(NamePtg namePtg);
}
