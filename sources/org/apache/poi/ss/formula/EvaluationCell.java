package org.apache.poi.ss.formula;

import org.apache.poi.ss.usermodel.CellType;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationCell.class */
public interface EvaluationCell {
    Object getIdentityKey();

    EvaluationSheet getSheet();

    int getRowIndex();

    int getColumnIndex();

    int getCellType();

    CellType getCellTypeEnum();

    double getNumericCellValue();

    String getStringCellValue();

    boolean getBooleanCellValue();

    int getErrorCellValue();

    int getCachedFormulaResultType();

    CellType getCachedFormulaResultTypeEnum();
}
