package org.apache.poi.ss.formula;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.util.Internal;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationWorkbook.class */
public interface EvaluationWorkbook {
    String getSheetName(int i);

    int getSheetIndex(EvaluationSheet evaluationSheet);

    int getSheetIndex(String str);

    EvaluationSheet getSheet(int i);

    ExternalSheet getExternalSheet(int i);

    ExternalSheet getExternalSheet(String str, String str2, int i);

    int convertFromExternSheetIndex(int i);

    ExternalName getExternalName(int i, int i2);

    ExternalName getExternalName(String str, String str2, int i);

    EvaluationName getName(NamePtg namePtg);

    EvaluationName getName(String str, int i);

    String resolveNameXText(NameXPtg nameXPtg);

    Ptg[] getFormulaTokens(EvaluationCell evaluationCell);

    UDFFinder getUDFFinder();

    SpreadsheetVersion getSpreadsheetVersion();

    void clearAllCachedResultValues();

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationWorkbook$ExternalSheet.class */
    public static class ExternalSheet {
        private final String _workbookName;
        private final String _sheetName;

        public ExternalSheet(String workbookName, String sheetName) {
            this._workbookName = workbookName;
            this._sheetName = sheetName;
        }

        public String getWorkbookName() {
            return this._workbookName;
        }

        public String getSheetName() {
            return this._sheetName;
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationWorkbook$ExternalSheetRange.class */
    public static class ExternalSheetRange extends ExternalSheet {
        private final String _lastSheetName;

        public ExternalSheetRange(String workbookName, String firstSheetName, String lastSheetName) {
            super(workbookName, firstSheetName);
            this._lastSheetName = lastSheetName;
        }

        public String getFirstSheetName() {
            return getSheetName();
        }

        public String getLastSheetName() {
            return this._lastSheetName;
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationWorkbook$ExternalName.class */
    public static class ExternalName {
        private final String _nameName;
        private final int _nameNumber;
        private final int _ix;

        public ExternalName(String nameName, int nameNumber, int ix) {
            this._nameName = nameName;
            this._nameNumber = nameNumber;
            this._ix = ix;
        }

        public String getName() {
            return this._nameName;
        }

        public int getNumber() {
            return this._nameNumber;
        }

        public int getIx() {
            return this._ix;
        }
    }
}
