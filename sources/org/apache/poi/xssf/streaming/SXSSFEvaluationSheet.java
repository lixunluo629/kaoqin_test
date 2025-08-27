package org.apache.poi.xssf.streaming;

import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.util.Internal;
import org.apache.poi.xssf.streaming.SXSSFFormulaEvaluator;

@Internal
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFEvaluationSheet.class */
final class SXSSFEvaluationSheet implements EvaluationSheet {
    private final SXSSFSheet _xs;

    public SXSSFEvaluationSheet(SXSSFSheet sheet) {
        this._xs = sheet;
    }

    public SXSSFSheet getSXSSFSheet() {
        return this._xs;
    }

    @Override // org.apache.poi.ss.formula.EvaluationSheet
    public EvaluationCell getCell(int rowIndex, int columnIndex) {
        SXSSFRow row = this._xs.getRow(rowIndex);
        if (row == null) {
            if (rowIndex <= this._xs.getLastFlushedRowNum()) {
                throw new SXSSFFormulaEvaluator.RowFlushedException(rowIndex);
            }
            return null;
        }
        SXSSFCell cell = row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }
        return new SXSSFEvaluationCell(cell, this);
    }

    @Override // org.apache.poi.ss.formula.EvaluationSheet
    public void clearAllCachedResultValues() {
    }
}
