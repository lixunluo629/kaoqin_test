package org.apache.poi.ss.usermodel;

import java.util.Map;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/FormulaEvaluator.class */
public interface FormulaEvaluator {
    void clearAllCachedResultValues();

    void notifySetFormula(Cell cell);

    void notifyDeleteCell(Cell cell);

    void notifyUpdateCell(Cell cell);

    void evaluateAll();

    CellValue evaluate(Cell cell);

    int evaluateFormulaCell(Cell cell);

    CellType evaluateFormulaCellEnum(Cell cell);

    Cell evaluateInCell(Cell cell);

    void setupReferencedWorkbooks(Map<String, FormulaEvaluator> map);

    void setIgnoreMissingWorkbooks(boolean z);

    void setDebugEvaluationOutputForNextEval(boolean z);
}
